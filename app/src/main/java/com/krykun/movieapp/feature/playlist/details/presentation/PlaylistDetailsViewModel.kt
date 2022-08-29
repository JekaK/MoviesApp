package com.krykun.movieapp.feature.playlist.details.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.krykun.domain.model.local.Playlist
import com.krykun.domain.usecase.local.GetPlaylistByIdUseCase
import com.krykun.movieapp.base.BaseViewModel
import com.krykun.movieapp.feature.moviedetails.presentation.MovieDetailsState
import com.krykun.movieapp.feature.tvseries.presentation.TvSeriesDetailsState
import com.krykun.movieapp.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailsViewModel @Inject constructor(
    appState: MutableStateFlow<AppState>,
    private val getAllPlaylistsUseCase: GetPlaylistByIdUseCase,
) : BaseViewModel<PlaylistDetailsSideEffects>(appState) {

    init {
        getPlaylistMovies()
    }

    val playlistInfo = mutableStateOf(Playlist())

    private fun getPlaylistMovies() = intent {
        viewModelScope.launch {
            getAllPlaylistsUseCase.getPlaylistById(container.stateFlow.value.value.playlistState.playlistDetailsState.playlistId)
                .collect { playlist ->
                    reduce {
                        state.value = state.value.copy(
                            playlistState = state.value.playlistState.copy(
                                playlistDetailsState = state.value.playlistState.playlistDetailsState.copy(
                                    playlist = playlist
                                )
                            )
                        )
                        state
                    }
                    postSideEffect(PlaylistDetailsSideEffects.UpdatePlaylistInfo(playlist))
                }
        }
    }

    fun navigateToMovieDetails(movieId: Int) = intent {
        reduce {
            state.value = state.value.copy(
                movieDetailsState = MovieDetailsState(id = movieId)
            )
            state
        }
        postSideEffect(PlaylistDetailsSideEffects.NavigateToMovieDetails)
    }

    fun navigateToTvDetailsDetails(tvId: Int) = intent {
        reduce {
            state.value = state.value.copy(
                tvSeriesState = TvSeriesDetailsState(id = tvId)
            )
            state
        }
        postSideEffect(PlaylistDetailsSideEffects.NavigateToTvDetails)
    }
}