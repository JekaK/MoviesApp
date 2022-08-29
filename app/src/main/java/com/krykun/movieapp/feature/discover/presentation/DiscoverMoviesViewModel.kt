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

    /**
     * `setScrollOffset` is a function that takes an `Int` and returns an `Intent` that reduces the
     * state by copying the current state, updating the `scrollOffsetUpcoming` property of the
     * `discoverMoviesState` property of the `homeState` property of the state with the given `Int`,
     * and then returning the state
     *
     * @param scrollOffset The scroll offset of the recycler view
     */
    fun setScrollOffset(scrollOffset: Int) = intent {
        reduce {
            state.value = state.value.copy(
                homeState = state.value.homeState.copy(
                    discoverMoviesState = state.value.homeState.discoverMoviesState.copy(
                        scrollOffset = scrollOffset
                    )
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
            state.value.homeState.discoverMoviesState.lastSavedPage > 0 -> state.value.homeState.discoverMoviesState.lastSavedPage
            state.value.homeState.discoverMoviesState.scrollOffset > 0f -> state.value.homeState.discoverMoviesState.scrollOffset.toInt()
            else -> 0
        }
        postSideEffect(
            DiscoverMoviesSideEffects.GetCurrentDiscoverPageAndScrollOffset(page)
        )
    }

    /**
     * > It sets the last scrolled page of the upcoming movies list
     *
     * @param index The index of the page that was last scrolled to.
     */
    fun setLastScrolledPage(index: Int) = intent {
        if (index != state.value.homeState.discoverMoviesState.lastSavedPage) {
            reduce {
                state.value = state.value.copy(
                    homeState = state.value.homeState.copy(
                        discoverMoviesState = state.value.homeState.discoverMoviesState.copy(
                            lastSavedPage = index
                        )
                    )
                )
                state
            }
        }
    }

    /**
     * > If the current page index is different than the index passed in, then update the state and
     * trigger the side effect
     *
     * @param index The index of the page that was selected.
     */
    fun triggerOnPageChanged(index: Int) = intent {
        if (index != state.value.homeState.discoverMoviesState.currentPageIndex) {
            reduce {
                state.value = state.value.copy(
                    homeState = state.value.homeState.copy(
                        discoverMoviesState = state.value.homeState.discoverMoviesState.copy(
                            currentPageIndex = index
                        )
                    )
                )
                state
            }
            postSideEffect(DiscoverMoviesSideEffects.TriggerOnPageChanged(index))
        }
    }

    /**
     * It takes a movieId and returns a function that takes a state and returns a new state with the
     * movieId set in the movieDetailsState
     *
     * @param movieId The id of the movie that we want to get the details for.
     */
    fun setMovieDetailsId(movieId: Int) = intent {
        reduce {
            state.value = state.value.copy(
                movieDetailsState = state.value.movieDetailsState.copy(
                    id = movieId
                ),
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
            postSideEffect(DiscoverMoviesSideEffects.TryReloadDiscoverPage)
        }
    }
}