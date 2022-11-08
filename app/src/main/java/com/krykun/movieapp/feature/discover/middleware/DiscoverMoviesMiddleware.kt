package com.krykun.movieapp.feature.discover.middleware

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.krykun.domain.model.remote.MovieDiscoverItem
import com.krykun.domain.usecase.remote.discover.GetDiscoverMoviesUseCase
import com.krykun.movieapp.base.BaseMiddleware
import com.krykun.movieapp.ext.takeWhenChanged
import com.krykun.movieapp.feature.discover.presentation.DiscoverMoviesSideEffects
import com.krykun.movieapp.feature.moviedetails.presentation.MovieDetailsState
import com.krykun.movieapp.state.AppState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.annotation.OrbitInternal
import javax.inject.Inject

class DiscoverMoviesMiddleware @Inject constructor(
    private val getDiscoverMoviesUseCase: GetDiscoverMoviesUseCase,
) : BaseMiddleware(), IDiscoverMoviesMiddleware {

    lateinit var getDiscoverMovies: Flow<PagingData<MovieDiscoverItem>>

    private lateinit var container: Container<MutableStateFlow<AppState>, DiscoverMoviesSideEffects>

    private lateinit var viewModelScope: CoroutineScope

    override fun init(
        container: Container<MutableStateFlow<AppState>, DiscoverMoviesSideEffects>,
        viewModelScope: CoroutineScope,
        callback: (Flow<PagingData<MovieDiscoverItem>>) -> Unit
    ) {
        this.container = container
        this.viewModelScope = viewModelScope

        var job: Job? = null
        job = viewModelScope.launch {
            container.stateFlow.value.takeWhenChanged {
                it.baseMoviesState.genres
            }.collect {
                getDiscoverMovies =
                    getDiscoverMoviesUseCase.getMovies(genres = it)
                        .cachedIn(viewModelScope)
                callback(getDiscoverMovies)
                job?.cancel()
            }
        }
    }


    /**
     * > Get the current page and scroll offset of the upcoming movies list
     */
    @OptIn(OrbitInternal::class)
    fun getCurrentPageAndScrollOffset() = viewModelScope.launch {
        container.orbit {
            val page = when {
                state.value.homeState.discoverMoviesState.lastSavedPage > 0 -> state.value.homeState.discoverMoviesState.lastSavedPage
                else -> 0
            }
            postSideEffect(
                DiscoverMoviesSideEffects.GetCurrentDiscoverPageAndScrollOffset(page)
            )
        }
    }

    /**
     * > It sets the last scrolled page of the upcoming movies list
     *
     * @param index The index of the page that was last scrolled to.
     */
    @OptIn(OrbitInternal::class)
    fun setLastScrolledPage(index: Int) = viewModelScope.launch {
        container.orbit {
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
    }

    /**
     * > If the current page index is different than the index passed in, then update the state and
     * trigger the side effect
     *
     * @param index The index of the page that was selected.
     */
    @OptIn(OrbitInternal::class)
    fun triggerOnPageChanged(index: Int) = viewModelScope.launch {
        container.orbit {
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
    }

    /**
     * It takes a movieId and returns a function that takes a state and returns a new state with the
     * movieId set in the movieDetailsState
     *
     * @param movieId The id of the movie that we want to get the details for.
     */
    @OptIn(OrbitInternal::class)
    fun navigateToMovieDetails(movieId: Int) = viewModelScope.launch {
        container.orbit {
            reduce {
                state.value = state.value.copy(
                    movieDetailsState = state.value.movieDetailsState + MovieDetailsState(id = movieId),
                )
                state
            }
            postSideEffect(DiscoverMoviesSideEffects.NavigateToMovie)
        }
    }
}