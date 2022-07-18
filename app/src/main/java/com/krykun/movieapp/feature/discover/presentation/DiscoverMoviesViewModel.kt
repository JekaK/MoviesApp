package com.krykun.movieapp.feature.discover.presentation

import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.krykun.domain.model.remote.MovieDiscoverItem
import com.krykun.domain.usecase.remote.discover.GetDiscoverMoviesUseCase
import com.krykun.movieapp.base.BaseViewModel
import com.krykun.movieapp.ext.takeWhenChanged
import com.krykun.movieapp.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

@HiltViewModel
class DiscoverMoviesViewModel @Inject constructor(
    appState: MutableStateFlow<AppState>,
    getDiscoverMoviesUseCase: GetDiscoverMoviesUseCase
) : BaseViewModel<DiscoverMoviesSideEffects>(appState) {

    lateinit var getDiscoverMovies: Flow<PagingData<MovieDiscoverItem>>

    init {
        var job: Job? = null
        job = viewModelScope.launch {
            container.stateFlow.value
                .takeWhenChanged {
                    it.baseMoviesState.genres
                }
                .collect {
                    getDiscoverMovies =
                        getDiscoverMoviesUseCase.getMovies(genres = appState.value.baseMoviesState.genres)
                            .cachedIn(scope = viewModelScope)
                    job?.cancel()
                }
        }
    }

    fun subscribeToStateUpdate() = container.stateFlow.value
        .takeWhenChanged {
            it.homeState.discoverMoviesState
        }

    fun setScrollOffset(scrollOffset: Float) = intent {
        reduce {
            state.value = state.value.copy(
                homeState = state.value.homeState.copy(
                    discoverMoviesState = state.value.homeState.discoverMoviesState.copy(
                        scrollOffsetUpcoming = scrollOffset
                    )
                )
            )
            state
        }
    }

    fun getCurrentPageAndScrollOffset() = intent {
        val page = when {
            state.value.homeState.discoverMoviesState.lastSavedPageUpcoming > 0 -> state.value.homeState.discoverMoviesState.lastSavedPageUpcoming
            state.value.homeState.discoverMoviesState.scrollOffsetUpcoming > 0f -> state.value.homeState.discoverMoviesState.scrollOffsetUpcoming.toInt()
            else -> 0
        }
        postSideEffect(
            DiscoverMoviesSideEffects.GetCurrentDiscoverPageAndScrollOffset(page)
        )
    }

    fun setLastScrolledPage(index: Int) = intent {
        if (index != state.value.homeState.discoverMoviesState.lastSavedPageUpcoming) {
            reduce {
                state.value = state.value.copy(
                    homeState = state.value.homeState.copy(
                        discoverMoviesState = state.value.homeState.discoverMoviesState.copy(
                            lastSavedPageUpcoming = index
                        )
                    )
                )
                state
            }
        }
    }

    fun triggerOnPageChanged(index: Int) = intent {
        if (index != state.value.homeState.discoverMoviesState.currentUpcomingPageIndex) {
            reduce {
                state.value = state.value.copy(
                    homeState = state.value.homeState.copy(
                        discoverMoviesState = state.value.homeState.discoverMoviesState.copy(
                            currentUpcomingPageIndex = index
                        )
                    )
                )
                state
            }
            postSideEffect(DiscoverMoviesSideEffects.TriggerOnPageChanged(index))
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

    fun setLoadingState(loadingState: LoadingState) = intent {
        reduce {
            state.value = state.value.copy(
                homeState = state.value.homeState.copy(
                    discoverMoviesState = state.value.homeState.discoverMoviesState.copy(
                        loadingState = loadingState
                    )
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
            postSideEffect(DiscoverMoviesSideEffects.TryReloadDiscoverPage)
        }
    }
}