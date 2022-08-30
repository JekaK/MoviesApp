package com.krykun.movieapp.feature.moviedetails.presentation

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.krykun.domain.model.remote.MovieDiscoverItem
import com.krykun.domain.model.remote.moviedetails.MovieDetails
import com.krykun.domain.usecase.remote.moviedetails.GetMovieCastDetailsUseCase
import com.krykun.domain.usecase.remote.moviedetails.GetMovieDetailsUseCase
import com.krykun.domain.usecase.remote.moviedetails.GetRecommendationsUseCase
import com.krykun.movieapp.base.BaseViewModel
import com.krykun.movieapp.ext.takeWhenChanged
import com.krykun.movieapp.feature.addtoplaylist.presentation.PlaylistSelectState
import com.krykun.movieapp.state.AppState
import com.krykun.movieapp.state.DetailsState
import com.krykun.movieapp.state.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
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
    private val getRecommendationsUseCase: GetRecommendationsUseCase
) : BaseViewModel<MovieDetailsSideEffects>(appState) {
    lateinit var getDiscoverMovies: Flow<PagingData<MovieDiscoverItem>>
    val movieData = mutableStateOf<MovieDetails?>(null)
    val movieDetailsState =
        mutableStateOf(DetailsState.LOADING)
    val isRatingVisible = mutableStateOf(false)

    init {
        var job: Job? = null
        job = viewModelScope.launch {
            container.stateFlow.value
                .takeWhenChanged {
                    it.baseMoviesState.genres
                }
                .collect {
                    getDiscoverMovies =
                        getRecommendationsUseCase.getRecommendations(
                            movieId = appState.value.movieDetailsState.last().id,
                            genres = appState.value.baseMoviesState.genres
                        ).cachedIn(scope = viewModelScope)
                    job?.cancel()
                }
        }
        loadMovieDetails()
    }

    fun clearSelectState() = intent {
        reduce {
            state.value = state.value.copy(
                playlistSelectState = if (state.value.movieDetailsState.size > 1) {
                    PlaylistSelectState(movieDetails = state.value.movieDetailsState[state.value.movieDetailsState.size - 1].details!!)
                } else {
                    PlaylistSelectState()
                },
                movieDetailsState = state.value.movieDetailsState - state.value.movieDetailsState.last()
            )
            state
        }
    }

    private fun loadMovieDetails() = intent {
        postSideEffect(MovieDetailsSideEffects.ShowLoadingState)
        val castResult =
            getMovieCastDetailsUseCase.getMovieCastDetails(movieId = state.value.movieDetailsState.last().id)
        val result =
            getMovieDetailsUseCase.getMovieDetail(movieId = state.value.movieDetailsState.last().id)

        if (result.isSuccess && castResult.isSuccess) {
            val verifiedResponse = result.map {
                it.copy(
                    cast = castResult.getOrNull()
                )
            }
            reduce {
                state.value = state.value.copy(
                    movieDetailsState = state.value.movieDetailsState.mapIndexed { index, movieDetailsState ->
                        if (index == state.value.movieDetailsState.size - 1) {
                            movieDetailsState.copy(
                                details = verifiedResponse.getOrNull()
                            )
                        } else {
                            movieDetailsState
                        }
                    }
                )
                state
            }
            postSideEffect(MovieDetailsSideEffects.ShowMovieData(verifiedResponse.getOrNull()))
        } else {
            postSideEffect(MovieDetailsSideEffects.ShowErrorState)
        }
    }

    private fun setMovieDetailsId(movieId: Int) = intent {
        reduce {
            state.value = state.value.copy(
                movieDetailsState = state.value.movieDetailsState + MovieDetailsState(id = movieId),
            )
            state
        }
    }

    fun updateMovieSelector() = intent {
        state.value.movieDetailsState.last().details?.let {
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

    fun navigateToMovie(movieId: Int) = intent {
        setMovieDetailsId(movieId)
        postSideEffect(MovieDetailsSideEffects.NavigateToMovie(movieId))
    }

    fun setLoadingState(loadingState: LoadingState) = intent {
        reduce {
            state.value = state.value.copy(
                movieDetailsState = state.value.movieDetailsState.mapIndexed { index, movieDetailsState ->
                    if (index == state.value.movieDetailsState.size - 1) {
                        movieDetailsState.copy(
                            loadingState = loadingState
                        )
                    } else {
                        movieDetailsState
                    }
                }
            )
            state
        }
    }

    /**
     * If any of the load states are in error, post a side effect to try reloading the page
     *
     * @param loadStates LoadStates - This is the LoadStates object that is passed to the
     * PagingDataAdapter.
     */
    fun handleLoadState(loadStates: LoadStates) = intent {
        val errorLoadState = arrayOf(
            loadStates.append,
            loadStates.prepend,
            loadStates.refresh
        ).filterIsInstance(LoadState.Error::class.java).firstOrNull()
        val throwable = errorLoadState?.error
        if (throwable != null) {
            postSideEffect(MovieDetailsSideEffects.TryReloadRecommendationsPage)
        }
    }
}