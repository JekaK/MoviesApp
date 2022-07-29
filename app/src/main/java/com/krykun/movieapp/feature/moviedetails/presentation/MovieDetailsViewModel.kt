package com.krykun.movieapp.feature.moviedetails.presentation

import android.annotation.SuppressLint
import androidx.lifecycle.viewModelScope
import com.krykun.domain.model.local.Playlist
import com.krykun.domain.usecase.local.AddPlaylistUseCase
import com.krykun.domain.usecase.remote.moviedetails.GetMovieCastDetailsUseCase
import com.krykun.domain.usecase.remote.moviedetails.GetMovieDetailsUseCase
import com.krykun.movieapp.base.BaseViewModel
import com.krykun.movieapp.feature.playlistselect.presentation.PlaylistSelectState
import com.krykun.movieapp.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    appState: MutableStateFlow<AppState>,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getMovieCastDetailsUseCase: GetMovieCastDetailsUseCase,
) : BaseViewModel<MovieDetailsSideEffects>(appState) {

    init {
        loadMovieDetails()
    }

    fun clearSelectState() = intent {
        reduce {
            state.value = state.value.copy(
                playlistSelectState = PlaylistSelectState()
            )
            state
        }
    }

    private fun loadMovieDetails() = intent {
        postSideEffect(MovieDetailsSideEffects.ShowLoadingState)
        val castResult =
            getMovieCastDetailsUseCase.getMovieCastDetails(state.value.movieDetailsState.movieId)
        val result = getMovieDetailsUseCase.getMovieDetail(state.value.movieDetailsState.movieId)

        if (result.isSuccess && castResult.isSuccess) {
            val verifiedResponse = result.map {
                it.copy(
                    cast = castResult.getOrNull()
                )
            }
            reduce {
                state.value = state.value.copy(
                    movieDetailsState = MovieDetailsState(
                        movieData = verifiedResponse.getOrNull()
                    )
                )
                state
            }
            postSideEffect(MovieDetailsSideEffects.ShowMovieData(verifiedResponse.getOrNull()))
        } else {
            postSideEffect(MovieDetailsSideEffects.ShowErrorState)
        }
    }

    fun updateMovieSelector() = intent {
        state.value.movieDetailsState.movieData?.let {
            reduce {
                state.value = state.value.copy(
                    playlistSelectState = state.value.playlistSelectState.copy(
                        movieDetails = it
                    )
                )
                state
            }
            postSideEffect(MovieDetailsSideEffects.OpenPlaylistSelector)
        }
    }
}