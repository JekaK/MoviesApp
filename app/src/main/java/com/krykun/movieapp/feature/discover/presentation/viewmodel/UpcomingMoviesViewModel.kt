package com.krykun.movieapp.feature.discover.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.krykun.domain.usecase.GetUpcomingMoviesUseCase
import com.krykun.movieapp.feature.discover.presentation.DiscoverMoviesSideEffects
import com.krykun.movieapp.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
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
) : ViewModel(),
    ContainerHost<MutableStateFlow<AppState>, DiscoverMoviesSideEffects> {

    override val container =
        container<MutableStateFlow<AppState>, DiscoverMoviesSideEffects>(appState)

    var currentPage = mutableStateOf(0)

    var scrollOffset = mutableStateOf(0f)

    val getDiscoverMovies =
        getDiscoverMoviesUseCase.getMovies(genres = appState.value.baseMoviesState.genres)
            .cachedIn(scope = viewModelScope)
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            )

    fun triggerOnPageChanged(index: Int) = intent {
        if (index != state.value.discoverMoviesState.currentPageIndex) {
            reduce {
                state.value = state.value.copy(
                    discoverMoviesState = state.value.discoverMoviesState.copy(
                        currentPageIndex = index
                    )
                )
                state
            }
            postSideEffect(DiscoverMoviesSideEffects.TriggerOnPageChanged(index))
        }
    }
}