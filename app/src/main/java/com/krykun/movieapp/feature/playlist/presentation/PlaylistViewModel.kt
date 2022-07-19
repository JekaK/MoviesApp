package com.krykun.movieapp.feature.playlist.presentation

import androidx.lifecycle.viewModelScope
import com.krykun.domain.usecase.local.GetAllPlaylistsUseCase
import com.krykun.movieapp.base.BaseViewModel
import com.krykun.movieapp.ext.takeWhenChanged
import com.krykun.movieapp.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    appState: MutableStateFlow<AppState>,
    private val getAllPlaylistsUseCase: GetAllPlaylistsUseCase
) : BaseViewModel<PlaylistSideEffects>(appState) {

    init {
        getAllPlaylists()
    }

    private fun getAllPlaylists() = intent {
        viewModelScope.launch {
            getAllPlaylistsUseCase.getAllPlaylists()
                .collect {
                    reduce {
                        state.value = state.value.copy(
                            playlistState = state.value.playlistState.copy(
                                playlists = it
                            )
                        )
                        state
                    }
                }
        }
    }

    fun subscribeToState() =
        container.stateFlow.value.takeWhenChanged {
            it.playlistState
        }
}