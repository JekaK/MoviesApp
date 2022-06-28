package com.krykun.movieapp.feature.discovermovies.presentation

import androidx.lifecycle.ViewModel
import com.krykun.domain.usecase.GetDiscoverMoviesUseCase
import com.krykun.movieapp.ext.takeWhenChanged
import com.krykun.movieapp.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class DiscoverMoviesViewModel @Inject constructor(
    appState: MutableStateFlow<AppState>,
    getDiscoverMoviesUseCase: GetDiscoverMoviesUseCase
) : ViewModel(), ContainerHost<MutableStateFlow<AppState>, DiscoverMoviesSideEffects> {

    override val container =
        container<MutableStateFlow<AppState>, DiscoverMoviesSideEffects>(appState)

    val getDiscoverMovies = getDiscoverMoviesUseCase.invoke()

    fun subscribeToStateUpdate() {
        container.stateFlow.value
            .takeWhenChanged {
                it.discoverMoviesState
            }
    }
}