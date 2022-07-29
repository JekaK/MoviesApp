package com.krykun.movieapp.feature.playlistselect.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.krykun.domain.model.local.Playlist
import com.krykun.domain.usecase.local.*
import com.krykun.movieapp.base.BaseViewModel
import com.krykun.movieapp.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

@HiltViewModel
class PlaylistSelectViewModel @Inject constructor(
    appState: MutableStateFlow<AppState>,
    private val addMovieToPlaylistUseCase: AddMovieToPlaylistUseCase,
    private val checkIsMovieAddedUseCase: CheckIsMovieAddedUseCase,
    private val getAllPlaylistsUseCase: GetAllPlaylistsUseCase,
    private val removeMovieFromPlaylistUseCase: RemoveMovieFromPlaylistUseCase,
    private val addPlaylistUseCase: AddPlaylistUseCase
) : BaseViewModel<PlaylistSelectSideEffects>(appState) {

    val playlistState = mutableStateOf(listOf<MappedPlaylist>())

    fun updateAllPlaylists() = intent {
        viewModelScope.launch(Dispatchers.IO) {
            val playlists = getAllPlaylistsUseCase.getAllPlaylists()
            val resultList = mutableListOf<MappedPlaylist>()
            playlists.forEach {
                val isInPlaylist = checkIsMovieAddedUseCase.checkIsMovieInPlaylist(
                    movieId = if (state.value.playlistSelectState.movieDetails.id != null) {
                        state.value.playlistSelectState.movieDetails.id ?: -1
                    } else {
                        state.value.playlistSelectState.tvDetails.id ?: -1
                    },
                    playlistId = it.playlistId.toInt()
                )
                resultList.add(
                    MappedPlaylist(
                        playlist = if (it.movieList.size >= 4) {
                            it.copy(movieList = it.movieList.subList(0, 4))
                        } else {
                            it.copy(movieList = it.movieList)
                        },
                        isMovieInPlaylist = isInPlaylist
                    )
                )
            }
            reduce {
                state.value = state.value.copy(
                    playlistSelectState = state.value.playlistSelectState.copy(
                        playlists = resultList
                    )
                )
                state
            }
            postSideEffect(PlaylistSelectSideEffects.UpdatePlaylistSelectList(resultList))
        }
    }

    private fun addMovieToPlaylist(playlistId: Long) = intent {
        if (state.value.playlistSelectState.movieDetails.id != null) {
            addMovieToPlaylistUseCase.insertMovieToPlaylist(
                playlistId = playlistId,
                movie = state.value.playlistSelectState.movieDetails
            )
        } else {
            addMovieToPlaylistUseCase.insertMovieToPlaylist(
                playlistId = playlistId,
                movie = state.value.playlistSelectState.tvDetails
            )
        }
    }

    private fun removeMovieFromPlaylist(playlistId: Long) = intent {
        if (state.value.playlistSelectState.movieDetails.id != null) {
            viewModelScope.launch {
                removeMovieFromPlaylistUseCase.removeMovieFromPlaylist(
                    playlistId = playlistId,
                    movie = state.value.playlistSelectState.movieDetails
                )
            }
        } else {
            viewModelScope.launch {
                removeMovieFromPlaylistUseCase.removeMovieFromPlaylist(
                    playlistId = playlistId,
                    movie = state.value.playlistSelectState.tvDetails
                )
            }
        }
    }

    fun changeMoviePlaylistStatus(playlistId: Long) = intent {
        if (state.value.playlistSelectState.playlists.find {
                it.playlist.playlistId == playlistId
            }?.isMovieInPlaylist == false) {
            addMovieToPlaylist(playlistId)
        } else {
            removeMovieFromPlaylist(playlistId)
        }
        updateAllPlaylists()
    }

    fun addPlaylist(name: String) {
        viewModelScope.launch {
            addPlaylistUseCase.addPlaylist(Playlist(name = name))
        }
    }
}