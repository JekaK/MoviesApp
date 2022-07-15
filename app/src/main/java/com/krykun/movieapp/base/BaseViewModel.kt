package com.krykun.movieapp.base

import androidx.lifecycle.ViewModel
import com.krykun.movieapp.state.AppState
import kotlinx.coroutines.flow.MutableStateFlow
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

open class BaseViewModel<T : Any> constructor(
    appState: MutableStateFlow<AppState>,
) : ViewModel(), ContainerHost<MutableStateFlow<AppState>, T> {

    override val container = container<MutableStateFlow<AppState>, T>(appState)
}