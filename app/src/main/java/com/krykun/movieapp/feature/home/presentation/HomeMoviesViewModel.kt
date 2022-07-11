package com.krykun.movieapp.feature.home.presentation

import androidx.lifecycle.ViewModel
import com.krykun.movieapp.ext.takeWhenChanged
import com.krykun.movieapp.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class HomeMoviesViewModel @Inject constructor(
    appState: MutableStateFlow<AppState>,
) : ViewModel(), ContainerHost<MutableStateFlow<AppState>, HomeMoviesSideEffects> {

    override val container =
        container<MutableStateFlow<AppState>, HomeMoviesSideEffects>(appState)

    fun subscribeToStateUpdate() {
        container.stateFlow.value
            .takeWhenChanged {
                it.homeState
            }
    }
}
