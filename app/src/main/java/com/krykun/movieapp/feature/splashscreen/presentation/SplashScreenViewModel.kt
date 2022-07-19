package com.krykun.movieapp.feature.splashscreen.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.krykun.domain.model.local.Playlist
import com.krykun.domain.usecase.local.AddPlaylistUseCase
import com.krykun.domain.usecase.local.GetAllPlaylistsUseCase
import com.krykun.domain.usecase.remote.moviedetails.GetMovieGenresUseCase
import com.krykun.domain.usecase.remote.tvdetails.GetTvGenresUseCase
import com.krykun.movieapp.base.BaseViewModel
import com.krykun.movieapp.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    appState: MutableStateFlow<AppState>,
    private val getMovieGenresUseCase: GetMovieGenresUseCase,
    private val getTvGenresUseCase: GetTvGenresUseCase,
    private val addPlaylistUseCase: AddPlaylistUseCase,
    private val getAllPlaylistsUseCase: GetAllPlaylistsUseCase
) : BaseViewModel<SplashScreenSideEffect>(appState) {

    private val splashDelay = 1000L

    val startAnimFlag = mutableStateOf(false)

    init {
        setScreenOpen()
        makeInitialDelay()
    }

    private fun setScreenOpen() = intent {
        reduce {
            state.value =
                state.value.copy(splashScreenState = state.value.splashScreenState.copy(isScreenOpen = true))
            state
        }
    }

    private fun insertDefaultPlaylist(callback: () -> Unit) = intent {
        var job: Job? = null
        job = viewModelScope.launch {
            getAllPlaylistsUseCase.getAllPlaylists()
                .collect {
                    if (it.isEmpty()) {
                        val playlist = Playlist(
                            name = "Favourite Movies",
                            movieList = listOf()
                        )
                        val playlistInsertResult = addPlaylistUseCase.addPlaylist(playlist)
                        if (playlistInsertResult >= 1) {
                            reduce {
                                state.value = state.value.copy(
                                    playlistState = state.value.playlistState.copy(
                                        playlists = listOf(playlist.copy(playlistId = playlistInsertResult))
                                    )
                                )
                                state
                            }
                            callback()
                            job?.cancel()
                        }
                    } else {
                        reduce {
                            state.value = state.value.copy(
                                playlistState = state.value.playlistState.copy(
                                    playlists = it
                                )
                            )
                            state
                        }
                        callback()
                        job?.cancel()
                    }
                }
        }
    }

    private fun makeInitialDelay() {
        insertDefaultPlaylist {
            intent {
                val response = getMovieGenresUseCase.getMovieGenres()
                val tvResponse = getTvGenresUseCase.getTvGenres()
                if (response.isSuccess &&
                    tvResponse.isSuccess
                ) {
                    reduce {
                        state.value = state.value.copy(
                            baseMoviesState = state.value.baseMoviesState.copy(
                                genres = (response.getOrNull() ?: listOf()) +
                                        (tvResponse.getOrNull() ?: listOf()),
                            )
                        )
                        state
                    }
                    delay(splashDelay)
                    postSideEffect(SplashScreenSideEffect.MoveToNextScreen)
                }
            }
        }
    }
}