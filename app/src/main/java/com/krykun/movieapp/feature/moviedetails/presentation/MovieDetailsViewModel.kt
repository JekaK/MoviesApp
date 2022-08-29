package com.krykun.movieapp.feature.moviedetails.presentation

import android.annotation.SuppressLint
import com.krykun.domain.usecase.remote.moviedetails.GetMovieCastDetailsUseCase
import com.krykun.domain.usecase.remote.moviedetails.GetMovieDetailsUseCase
import com.krykun.movieapp.base.BaseViewModel
import com.krykun.movieapp.feature.playlistselect.presentation.PlaylistSelectState
import com.krykun.movieapp.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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
            getMovieCastDetailsUseCase.getMovieCastDetails(state.value.movieDetailsState.id)
        val result = getMovieDetailsUseCase.getMovieDetail(state.value.movieDetailsState.id)

        if (result.isSuccess && castResult.isSuccess) {
            val verifiedResponse = result.map {
                it.copy(
                    cast = castResult.getOrNull()
                )
            }
            reduce {
                state.value = state.value.copy(
                    movieDetailsState = MovieDetailsState(
                        details = verifiedResponse.getOrNull()
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
        state.value.movieDetailsState.details?.let {
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