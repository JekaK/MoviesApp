package com.krykun.movieapp.feature.moviedetails.presentation

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.krykun.movieapp.feature.playlistselect.presentation.PlaylistSelectState
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
                            movieId = appState.value.movieDetailsState.id,
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
                playlistSelectState = PlaylistSelectState()
            )
            state
        }
    }

    private fun loadMovieDetails() = intent {
        postSideEffect(MovieDetailsSideEffects.ShowLoadingState)
        val castResult =
            getMovieCastDetailsUseCase.getMovieCastDetails(movieId = state.value.movieDetailsState.id)
        val result =
            getMovieDetailsUseCase.getMovieDetail(movieId = state.value.movieDetailsState.id)

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

    fun setLastScrolledPage(index: Int) = intent {
        if (index != state.value.movieDetailsState.lastSavedPage) {
            reduce {
                state.value = state.value.copy(
                    movieDetailsState = state.value.movieDetailsState.copy(
                        lastSavedPage = index
                    )
                )
                state
            }
        }
    }

    fun setScrollOffset(scrollOffset: Int) = intent {
        reduce {
            state.value = state.value.copy(
                movieDetailsState = state.value.movieDetailsState.copy(
                    scrollOffset = scrollOffset
                )
            )
            state
        }
    }

    /**
     * > Get the current page and scroll offset of the upcoming movies list
     */
    fun getCurrentPageAndScrollOffset() = intent {
        val page = when {
            state.value.movieDetailsState.lastSavedPage > 0 -> state.value.movieDetailsState.lastSavedPage
            state.value.movieDetailsState.scrollOffset > 0f -> state.value.movieDetailsState.scrollOffset
            else -> 0
        }
        postSideEffect(
            MovieDetailsSideEffects.GetCurrentPageAndScrollOffset(page)
        )
    }

    fun setLoadingState(loadingState: LoadingState) = intent {
        reduce {
            state.value = state.value.copy(
                movieDetailsState = state.value.movieDetailsState.copy(
                    loadingState = loadingState
                )
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