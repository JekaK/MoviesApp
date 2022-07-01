package com.krykun.movieapp.feature.discover.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.krykun.movieapp.ext.takeWhenChanged
import com.krykun.movieapp.feature.discover.presentation.DiscoverMoviesSideEffects
import com.krykun.movieapp.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class DiscoverMoviesViewModel @Inject constructor(
    appState: MutableStateFlow<AppState>,
) : ViewModel(), ContainerHost<MutableStateFlow<AppState>, DiscoverMoviesSideEffects> {

    override val container =
        container<MutableStateFlow<AppState>, DiscoverMoviesSideEffects>(appState)

    fun subscribeToStateUpdate() {
        container.stateFlow.value
            .takeWhenChanged {
                it.discoverMoviesState
            }
    }
}
