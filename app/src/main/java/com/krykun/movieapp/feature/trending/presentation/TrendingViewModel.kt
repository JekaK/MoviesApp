package com.krykun.movieapp.feature.trending.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.krykun.domain.model.movies.Movie
import com.krykun.domain.usecase.GetPopularMoviesUseCase
import com.krykun.domain.usecase.GetTrendingMoviesUseCase
import com.krykun.movieapp.ext.takeWhenChanged
import com.krykun.movieapp.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class TrendingViewModel @Inject constructor(
    private val appState: MutableStateFlow<AppState>,
    getTrendingMoviesUseCase: GetTrendingMoviesUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : ViewModel(), ContainerHost<MutableStateFlow<AppState>, TrendingMoviesSideEffects> {

    override val container =
        container<MutableStateFlow<AppState>, TrendingMoviesSideEffects>(appState)

    lateinit var getTrendingMovies: Flow<PagingData<Movie>>
    var getPopularMovies: Flow<PagingData<Movie>>? = null

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
                            .shareIn(
                                scope = viewModelScope,
                                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                                replay = 1
                            )
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
                                    .shareIn(
                                        scope = viewModelScope,
                                        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                                        replay = 1
                                    )
                        }
                    }
                }
                it.toTrendingProps()
            }

    private fun TrendingMoviesState.toTrendingProps(): TrendingProps {
        return TrendingProps(
            currentTrendingPageIndex = this.currentTrendingPageIndex,
            scrollOffsetTrending = this.scrollOffsetTrending,
            lastSavedPageTrending = this.lastSavedPageTrending,
            isShowLoading = when (this.selectedMovieType) {
                is SelectedMovieType.TRENDING -> {
                    this.trendingMovieType.loadingState == LoadingState.LOADING
                }
                is SelectedMovieType.POPULAR -> {
                    this.popularMovieType.loadingState == LoadingState.LOADING
                }
            }
        )
    }

    fun setSelectedMovieType(
        selectedMovieType: SelectedMovieType,
        isSelecting: Boolean = true
    ) = intent {
        val selectedMovie: SelectedMovieType = when (selectedMovieType) {
            is SelectedMovieType.TRENDING -> {
                val currentMovieType =
                    state.value.homeState.trendingMoviesState.trendingMovieType
                if (currentMovieType.loadingState == LoadingState.STATIONARY) {
                    selectedMovieType.copy(loadingState = currentMovieType.loadingState)
                } else {
                    selectedMovieType
                }
            }
            is SelectedMovieType.POPULAR -> {
                val currentMovieType =
                    state.value.homeState.trendingMoviesState.popularMovieType
                if (currentMovieType.loadingState == LoadingState.STATIONARY) {
                    selectedMovieType.copy(loadingState = currentMovieType.loadingState)
                } else {
                    selectedMovieType
                }
            }
        }

        if (selectedMovie is SelectedMovieType.TRENDING) {
            reduce {
                state.value = state.value.copy(
                    homeState = state.value.homeState.copy(
                        trendingMoviesState = state.value.homeState.trendingMoviesState.copy(
                            trendingMovieType = selectedMovie,
                            selectedMovieType = if (isSelecting) {
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
            reduce {
                state.value = state.value.copy(
                    homeState = state.value.homeState.copy(
                        trendingMoviesState = state.value.homeState.trendingMoviesState.copy(
                            popularMovieType = selectedMovie,
                            selectedMovieType = if (isSelecting) {
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
        if (isSelecting) {
            postSideEffect(
                TrendingMoviesSideEffects.ChangeMoviesSelectedItem(
                    selectedMovieType = selectedMovie
                )
            )
        }
    }

    fun setScrollOffset(scrollOffset: Float) = intent {
        reduce {
            state.value = state.value.copy(
                homeState = state.value.homeState.copy(
                    trendingMoviesState = state.value.homeState.trendingMoviesState.copy(
                        scrollOffsetTrending = scrollOffset
                    )
                )
            )
            state
        }
    }

    fun getCurrentPageAndScrollOffset() = intent {
        val page = when {
            state.value.homeState.trendingMoviesState.lastSavedPageTrending > 0 -> state.value.homeState.trendingMoviesState.lastSavedPageTrending
            state.value.homeState.trendingMoviesState.scrollOffsetTrending > 0f -> state.value.homeState.trendingMoviesState.scrollOffsetTrending.toInt()
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
                        lastSavedPageTrending = index
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
                    movieId = movieId
                )
            )
            state
        }
    }

    fun handleLoadState(loadStates: LoadStates) = intent {
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

}