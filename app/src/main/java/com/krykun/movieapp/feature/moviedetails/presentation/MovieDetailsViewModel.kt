package com.krykun.movieapp.feature.moviedetails.presentation

import androidx.lifecycle.viewModelScope
import com.krykun.domain.usecase.local.AddMovieToPlaylistUseCase
import com.krykun.domain.usecase.local.CheckIsMovieAddedUseCase
import com.krykun.domain.usecase.remote.moviedetails.GetMovieCastDetailsUseCase
import com.krykun.domain.usecase.remote.moviedetails.GetMovieDetailsUseCase
import com.krykun.movieapp.base.BaseViewModel
import com.krykun.movieapp.ext.takeWhenChanged
import com.krykun.movieapp.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    appState: MutableStateFlow<AppState>,
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getMovieCastDetailsUseCase: GetMovieCastDetailsUseCase,
    private val addMovieToPlaylistUseCase: AddMovieToPlaylistUseCase,
    private val checkIsMovieAddedUseCase: CheckIsMovieAddedUseCase
) : BaseViewModel<MovieDetailsSideEffects>(appState) {

    init {
        loadMovieDetails()
        checkIsAdded()
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

    private fun checkIsAdded() = intent {
        viewModelScope.launch {
            checkIsMovieAddedUseCase.checkIsMovieInPlaylist(container.stateFlow.value.value.movieDetailsState.movieId)
                .collect {
                    reduce {
                        state.value = state.value.copy(
                            movieDetailsState = state.value.movieDetailsState.copy(
                                isAdded = it
                            )
                        )
                        state
                    }
                    postSideEffect(sideEffect = MovieDetailsSideEffects.UpdateIsAddedState(it))
                }
        }
    }

    fun addMovie() = intent {
        state.value.movieDetailsState.movieData?.let {
            addMovieToPlaylistUseCase.insertMovieToPlaylist(
                movie = it,
                playlistId = state.value.playlistState.playlists[0].playlistId
            )
        }
    }
}