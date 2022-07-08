package com.krykun.movieapp.feature.upcoming.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.krykun.domain.model.MovieDiscoverItem
import com.krykun.domain.usecase.GetUpcomingMoviesUseCase
import com.krykun.movieapp.ext.takeWhenChanged
import com.krykun.movieapp.feature.discover.presentation.DiscoverMoviesSideEffects
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
class UpcomingMoviesViewModel @Inject constructor(
    appState: MutableStateFlow<AppState>,
    getDiscoverMoviesUseCase: GetUpcomingMoviesUseCase
) : ViewModel(), ContainerHost<MutableStateFlow<AppState>, UpcomingMoviesSideEffects> {

    override val container =
        container<MutableStateFlow<AppState>, UpcomingMoviesSideEffects>(appState)

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
                    upcomingMoviesState = state.value.discoverMoviesState.upcomingMoviesState.copy(
                        scrollOffsetUpcoming = scrollOffset
                    )
                )
            )
            state
        }
    }

    fun getCurrentPageAndScrollOffset() = intent {
        val page = when {
            state.value.discoverMoviesState.upcomingMoviesState.lastSavedPageUpcoming > 0 -> state.value.discoverMoviesState.upcomingMoviesState.lastSavedPageUpcoming
            state.value.discoverMoviesState.upcomingMoviesState.scrollOffsetUpcoming > 0f -> state.value.discoverMoviesState.upcomingMoviesState.scrollOffsetUpcoming.toInt()
            else -> 0
        }
        postSideEffect(
            UpcomingMoviesSideEffects.GetCurrentUpcomingPageAndScrollOffset(page)
        )
    }

    fun setLastScrolledPage(index: Int) = intent {
        if (index != state.value.discoverMoviesState.upcomingMoviesState.lastSavedPageUpcoming) {
            reduce {
                state.value = state.value.copy(
                    discoverMoviesState = state.value.discoverMoviesState.copy(
                        upcomingMoviesState = state.value.discoverMoviesState.upcomingMoviesState.copy(
                            lastSavedPageUpcoming = index
                        )
                    )
                )
                state
            }
        }
    }

    fun triggerOnPageChanged(index: Int) = intent {
        if (index != state.value.discoverMoviesState.upcomingMoviesState.currentUpcomingPageIndex) {
            reduce {
                state.value = state.value.copy(
                    discoverMoviesState = state.value.discoverMoviesState.copy(
                        upcomingMoviesState = state.value.discoverMoviesState.upcomingMoviesState.copy(
                            currentUpcomingPageIndex = index
                        )
                    )
                )
                state
            }
            postSideEffect(UpcomingMoviesSideEffects.TriggerOnPageChanged(index))
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
            postSideEffect(UpcomingMoviesSideEffects.TryReloadUpcomingPage)
        }
    }
}