package com.krykun.movieapp.feature.trending.presentation

import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.krykun.domain.model.remote.movies.Movie
import com.krykun.domain.usecase.remote.filteredmovies.GetPopularMoviesUseCase
import com.krykun.domain.usecase.remote.filteredmovies.GetTopRatedMoviesUseCase
import com.krykun.domain.usecase.remote.filteredmovies.GetTrendingMoviesUseCase
import com.krykun.movieapp.base.BaseViewModel
import com.krykun.movieapp.ext.takeWhenChanged
import com.krykun.movieapp.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

@HiltViewModel
class TrendingViewModel @Inject constructor(
    private val appState: MutableStateFlow<AppState>,
    getTrendingMoviesUseCase: GetTrendingMoviesUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val getTopRatedMoviesUseCase: GetTopRatedMoviesUseCase
) : BaseViewModel<TrendingMoviesSideEffects>(appState) {

    lateinit var getTrendingMovies: Flow<PagingData<Movie>>
    var getPopularMovies: Flow<PagingData<Movie>>? = null
    var getTopRatedMovies: Flow<PagingData<Movie>>? = null

    init {
        var job: Job? = null
        job = viewModelScope.launch {
            container.stateFlow.value
                .takeWhenChanged {
                    it.baseMoviesState.genres
                }
                .collect {
                    getTrendingMovies =
                        getTrendingMoviesUseCase.getTrendingMovies(genres = appState.value.baseMoviesState.genres)
                            .cachedIn(scope = viewModelScope)
                    job?.cancel()
                }
        }
    }

    fun subscribeToStateUpdate() =
        container.stateFlow.value
            .takeWhenChanged {
                it.homeState.trendingMoviesState
            }
            .map {
                when (it.selectedMovieType) {
                    SelectedMovieType.POPULAR() -> {
                        if (getPopularMovies == null) {
                            getPopularMovies =
                                getPopularMoviesUseCase.getPopularMovies(genres = appState.value.baseMoviesState.genres)
                                    .cachedIn(scope = viewModelScope)
                        }
                    }
                    SelectedMovieType.TOPRATED() -> {
                        if (getTopRatedMovies == null) {
                            getTopRatedMovies =
                                getTopRatedMoviesUseCase.getTopRatedMovies(genres = appState.value.baseMoviesState.genres)
                                    .cachedIn(scope = viewModelScope)
                        }
                    }
                }
                it.toTrendingProps()
            }

    private fun TrendingMoviesState.toTrendingProps(): TrendingProps {
        return TrendingProps(
            currentTrendingPageIndex = this.currentPageIndex,
            scrollOffsetTrending = this.scrollOffset,
            lastSavedPageTrending = this.lastSavedPage,
            isShowLoading = when (this.selectedMovieType) {
                is SelectedMovieType.TRENDING -> {
                    this.trendingMovieType.loadingState == LoadingState.LOADING
                }
                is SelectedMovieType.POPULAR -> {
                    this.popularMovieType.loadingState == LoadingState.LOADING
                }
                is SelectedMovieType.TOPRATED -> {
                    this.topRatedMovieType.loadingState == LoadingState.LOADING
                }
            },
            selectedMovieType = this.selectedMovieType
        )
    }

    private fun updateSelectedMovie(
        state: AppState,
        selectedMovieType: SelectedMovieType,
    ): SelectedMovieType {
        val selectedMovie: SelectedMovieType = when (selectedMovieType) {
            is SelectedMovieType.TRENDING -> {
                val currentMovieType = state.homeState.trendingMoviesState.trendingMovieType
                if (currentMovieType.loadingState == LoadingState.STATIONARY) {
                    selectedMovieType.copy(loadingState = currentMovieType.loadingState)
                } else {
                    selectedMovieType
                }
            }
            is SelectedMovieType.POPULAR -> {
                val currentMovieType = state.homeState.trendingMoviesState.popularMovieType
                if (currentMovieType.loadingState == LoadingState.STATIONARY) {
                    selectedMovieType.copy(loadingState = currentMovieType.loadingState)
                } else {
                    selectedMovieType
                }
            }
            is SelectedMovieType.TOPRATED -> {
                val currentMovieType = state.homeState.trendingMoviesState.topRatedMovieType
                if (currentMovieType.loadingState == LoadingState.STATIONARY) {
                    selectedMovieType.copy(loadingState = currentMovieType.loadingState)
                } else {
                    selectedMovieType
                }
            }
        }
        return selectedMovie
    }

    fun setSelectedMovieType(
        selectedMovieType: SelectedMovieType,
        isSelecting: Boolean = true
    ) = intent {
        val selectedMovie: SelectedMovieType = updateSelectedMovie(state.value, selectedMovieType)

        if (selectedMovie is SelectedMovieType.TRENDING) {
            if (isSelecting &&
                state.value.homeState.trendingMoviesState.selectedMovieType !is SelectedMovieType.TRENDING
            ) {
                postSideEffect(TrendingMoviesSideEffects.ChangeMoviesSelectedItem)
            }
            reduce {
                state.value = state.value.copy(
                    homeState = state.value.homeState.copy(
                        trendingMoviesState = state.value.homeState.trendingMoviesState.copy(
                            trendingMovieType = selectedMovie,
                            selectedMovieType = if (isSelecting &&
                                state.value.homeState.trendingMoviesState.selectedMovieType !is SelectedMovieType.TRENDING
                            ) {
                                selectedMovie
                            } else {
                                state.value.homeState.trendingMoviesState.selectedMovieType
                            }
                        )
                    )
                )
                state
            }
        }

        if (selectedMovie is SelectedMovieType.POPULAR) {
            if (isSelecting &&
                state.value.homeState.trendingMoviesState.selectedMovieType !is SelectedMovieType.POPULAR
            ) {
                postSideEffect(TrendingMoviesSideEffects.ChangeMoviesSelectedItem)
            }
            reduce {
                state.value = state.value.copy(
                    homeState = state.value.homeState.copy(
                        trendingMoviesState = state.value.homeState.trendingMoviesState.copy(
                            popularMovieType = selectedMovie,
                            selectedMovieType = if (isSelecting &&
                                state.value.homeState.trendingMoviesState.selectedMovieType !is SelectedMovieType.POPULAR
                            ) {
                                selectedMovie
                            } else {
                                state.value.homeState.trendingMoviesState.selectedMovieType
                            }
                        )
                    )
                )
                state
            }
        }

        if (selectedMovie is SelectedMovieType.TOPRATED) {
            if (isSelecting &&
                state.value.homeState.trendingMoviesState.selectedMovieType !is SelectedMovieType.TOPRATED
            ) {
                postSideEffect(TrendingMoviesSideEffects.ChangeMoviesSelectedItem)
            }
            reduce {
                state.value = state.value.copy(
                    homeState = state.value.homeState.copy(
                        trendingMoviesState = state.value.homeState.trendingMoviesState.copy(
                            topRatedMovieType = selectedMovie,
                            selectedMovieType = if (isSelecting &&
                                state.value.homeState.trendingMoviesState.selectedMovieType !is SelectedMovieType.TOPRATED
                            ) {
                                selectedMovie
                            } else {
                                state.value.homeState.trendingMoviesState.selectedMovieType
                            }
                        )
                    )
                )
                state
            }
        }
    }

    fun setScrollOffset(scrollOffset: Float) = intent {
        reduce {
            state.value = state.value.copy(
                homeState = state.value.homeState.copy(
                    trendingMoviesState = state.value.homeState.trendingMoviesState.copy(
                        scrollOffset = scrollOffset
                    )
                )
            )
            state
        }
    }

    fun getCurrentPageAndScrollOffset() = intent {
        val page = when {
            state.value.homeState.trendingMoviesState.lastSavedPage > 0 -> state.value.homeState.trendingMoviesState.lastSavedPage
            state.value.homeState.trendingMoviesState.scrollOffset > 0f -> state.value.homeState.trendingMoviesState.scrollOffset.toInt()
            else -> 0
        }
        postSideEffect(
            TrendingMoviesSideEffects.GetCurrentTrendingPageAndScrollOffset(page)
        )
    }

    fun setLastScrolledPage(index: Int) = intent {
        reduce {
            state.value = state.value.copy(
                homeState = state.value.homeState.copy(
                    trendingMoviesState = state.value.homeState.trendingMoviesState.copy(
                        lastSavedPage = index
                    )
                )
            )
            state
        }
    }

    fun setMovieDetailsId(movieId: Int) = intent {
        reduce {
            state.value = state.value.copy(
                movieDetailsState = state.value.movieDetailsState.copy(
                    id = movieId
                )
            )
            state
        }
    }

    fun handleLoadTrendingState(loadStates: LoadStates) = intent {
        val errorLoadState = arrayOf(
            loadStates.append,
            loadStates.prepend,
            loadStates.refresh
        ).filterIsInstance(LoadState.Error::class.java).firstOrNull()
        val throwable = errorLoadState?.error
        if (throwable != null) {
            postSideEffect(TrendingMoviesSideEffects.TryReloadTrendingPage)
        }
    }

    fun handleLoadPopularState(loadStates: LoadStates) = intent {
        val errorLoadState = arrayOf(
            loadStates.append,
            loadStates.prepend,
            loadStates.refresh
        ).filterIsInstance(LoadState.Error::class.java).firstOrNull()
        val throwable = errorLoadState?.error
        if (throwable != null) {
            postSideEffect(TrendingMoviesSideEffects.TryReloadPopularPage)
        }
    }

    fun handleLoadTopRatedState(loadStates: LoadStates) = intent {
        val errorLoadState = arrayOf(
            loadStates.append,
            loadStates.prepend,
            loadStates.refresh
        ).filterIsInstance(LoadState.Error::class.java).firstOrNull()
        val throwable = errorLoadState?.error
        if (throwable != null) {
            postSideEffect(TrendingMoviesSideEffects.TryReloadTopRatedPage)
        }
    }
}