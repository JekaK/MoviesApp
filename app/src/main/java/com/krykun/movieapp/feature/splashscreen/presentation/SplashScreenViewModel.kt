package com.krykun.movieapp.feature.splashscreen.presentation

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.krykun.domain.model.local.Playlist
import com.krykun.domain.usecase.local.AddPlaylistUseCase
import com.krykun.domain.usecase.local.GetAllPlaylistsUseCase
import com.krykun.domain.usecase.remote.moviedetails.GetMovieGenresUseCase
import com.krykun.domain.usecase.remote.tvdetails.GetTvGenresUseCase
import com.krykun.movieapp.R
import com.krykun.movieapp.base.BaseViewModel
import com.krykun.movieapp.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

@HiltViewModel
@SuppressLint("StaticFieldLeak")
class SplashScreenViewModel @Inject constructor(
    private val context: Context,
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

    private fun insertDefaultPlaylist(callback: SimpleSyntax<MutableStateFlow<AppState>, SplashScreenSideEffect>.() -> Unit) =
        intent {
            var job: Job? = null
            job = viewModelScope.launch(Dispatchers.IO) {
                getAllPlaylistsUseCase.getAllPlaylists()
                    .collect {
                        var playlistMoviesInsertResult: Long = 0
                        var moviesPlaylist: Playlist? = null
                        var tvSeriesPlaylist: Playlist? = null
                        var playlistTvSeriesInsertResult: Long = 0
                        if (it.find {
                                it.name == context.getString(R.string.favourite_movies)
                            } == null) {
                            moviesPlaylist = Playlist(
                                name = context.getString(R.string.favourite_movies),
                                movieList = listOf()
                            )

                            playlistMoviesInsertResult =
                                addPlaylistUseCase.addPlaylist(moviesPlaylist)
                        }
                        if (it.find {
                                it.name == context.getString(R.string.favourite_tv_series)
                            } == null) {
                            tvSeriesPlaylist = Playlist(
                                name = context.getString(R.string.favourite_tv_series),
                                movieList = listOf()
                            )
                            playlistTvSeriesInsertResult =
                                addPlaylistUseCase.addPlaylist(tvSeriesPlaylist)
                        }
                        if (playlistMoviesInsertResult >= 1) {
                            moviesPlaylist?.copy(
                                playlistId = playlistMoviesInsertResult
                            )?.let {
                                reduce {
                                    state.value = state.value.copy(
                                        playlistState = state.value.playlistState.copy(
                                            playlists = state.value.playlistState.playlists + it

                                        )
                                    )
                                    state
                                }
                            }
                        }

                        if (playlistTvSeriesInsertResult >= 1) {
                            tvSeriesPlaylist?.copy(
                                playlistId = playlistTvSeriesInsertResult
                            )?.let {
                                reduce {
                                    state.value = state.value.copy(
                                        playlistState = state.value.playlistState.copy(
                                            playlists = state.value.playlistState.playlists + it

                                        )
                                    )
                                    state
                                }
                            }
                        }
                        if (it.isNotEmpty()) {
                            reduce {
                                state.value = state.value.copy(
                                    playlistState = state.value.playlistState.copy(
                                        playlists = state.value.playlistState.playlists + it
                                    )
                                )
                                state
                            }
                        }
                        callback(this@intent)
                        job?.cancel()
                    }
            }
        }

    private fun makeInitialDelay() {
        insertDefaultPlaylist {
            viewModelScope.launch(Dispatchers.IO) {
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