package com.krykun.movieapp.feature.playlist.main.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.krykun.domain.model.local.Playlist
import com.krykun.domain.usecase.local.AddPlaylistUseCase
import com.krykun.domain.usecase.local.GetAllPlaylistsUseCase
import com.krykun.domain.usecase.local.GetPlaylistMoviesByLimit
import com.krykun.domain.usecase.local.RemovePlaylistUseCase
import com.krykun.movieapp.base.BaseViewModel
import com.krykun.movieapp.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    appState: MutableStateFlow<AppState>,
    private val getAllPlaylistsUseCase: GetAllPlaylistsUseCase,
    private val addPlaylistUseCase: AddPlaylistUseCase,
    private val removePlaylistUseCase: RemovePlaylistUseCase
) : BaseViewModel<PlaylistSideEffects>(appState) {

    init {
        getAllPlaylists()
    }

    val playlistState = mutableStateOf(listOf<Playlist>())

    private fun getAllPlaylists() = intent {
        viewModelScope.launch {
            getAllPlaylistsUseCase.getAllPlaylistsFlow()
                .collect {
                    reduce {
                        state.value = state.value.copy(
                            playlistState = state.value.playlistState.copy(
                                playlists = it
                            )
                        )
                        state
                    }
                    postSideEffect(PlaylistSideEffects.UpdatePlaylist(it))
                }
        }
    }

    fun navigateToPlaylistDetails(playlistId: Long) = intent {
        reduce {
            state.value = state.value.copy(
                playlistState = state.value.playlistState.copy(
                    playlistDetailsState = state.value.playlistState.playlistDetailsState.copy(
                        playlistId = playlistId
                    )
                )
            )
            state
        }
        postSideEffect(PlaylistSideEffects.NavigateToPlaylistDetails)
    }

    fun addPlaylist(name: String) {
        viewModelScope.launch {
            addPlaylistUseCase.addPlaylist(Playlist(name = name))
        }
    }

    fun removePlaylist(playlistId: Long) {
        viewModelScope.launch {
            removePlaylistUseCase.removePlaylist(playlistId = playlistId)
        }
    }
}