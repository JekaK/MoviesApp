package com.krykun.movieapp.feature.trending.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.krykun.domain.model.trending.TrendingMovie
import com.krykun.domain.usecase.GetTrendingMoviesUseCase
import com.krykun.movieapp.ext.takeWhenChanged
import com.krykun.movieapp.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class TrendingViewModel @Inject constructor(
    appState: MutableStateFlow<AppState>,
    getTrendingMoviesUseCase: GetTrendingMoviesUseCase
) : ViewModel(), ContainerHost<MutableStateFlow<AppState>, TrendingMoviesSideEffects> {

    override val container =
        container<MutableStateFlow<AppState>, TrendingMoviesSideEffects>(appState)

    lateinit var getTrendingMovies: Flow<PagingData<TrendingMovie>>

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

    fun setScrollOffset(scrollOffset: Float) = intent {
        reduce {
            state.value = state.value.copy(
                discoverMoviesState = state.value.discoverMoviesState.copy(
                    trendingMoviesState = state.value.discoverMoviesState.trendingMoviesState.copy(
                        scrollOffsetTrending = scrollOffset
                    )
                )
            )
            state
        }
    }

    fun getCurrentPageAndScrollOffset() = intent {
        val page = when {
            state.value.discoverMoviesState.trendingMoviesState.lastSavedPageTrending > 0 -> state.value.discoverMoviesState.trendingMoviesState.lastSavedPageTrending
            state.value.discoverMoviesState.trendingMoviesState.scrollOffsetTrending > 0f -> state.value.discoverMoviesState.trendingMoviesState.scrollOffsetTrending.toInt()
            else -> 0
        }
        postSideEffect(
            TrendingMoviesSideEffects.GetCurrentTrendingPageAndScrollOffset(page)
        )
    }

    fun setLastScrolledPage(index: Int) = intent {
        if (index != state.value.discoverMoviesState.trendingMoviesState.lastSavedPageTrending) {
            reduce {
                state.value = state.value.copy(
                    discoverMoviesState = state.value.discoverMoviesState.copy(
                        trendingMoviesState = state.value.discoverMoviesState.trendingMoviesState.copy(
                            lastSavedPageTrending = index
                        )
                    )
                )
                state
            }
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