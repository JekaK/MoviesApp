package com.krykun.movieapp.feature.playlistselect.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.krykun.domain.usecase.local.AddMovieToPlaylistUseCase
import com.krykun.domain.usecase.local.CheckIsMovieAddedUseCase
import com.krykun.domain.usecase.local.GetAllPlaylistsUseCase
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
    private val getAllPlaylistsUseCase: GetAllPlaylistsUseCase
) : BaseViewModel<PlaylistSelectSideEffects>(appState) {

    val playlistState = mutableStateOf(listOf<MappedPlaylist>())

    init {
        getAllPlaylists()
    }

    private fun getAllPlaylists() = intent {
        viewModelScope.launch {
            getAllPlaylistsUseCase.getAllPlaylists()
                .collect {
                    viewModelScope.launch(Dispatchers.IO) {
                        val resultList = mutableListOf<MappedPlaylist>()
                        it.forEach {
                            val isInPlaylist =
                                checkIsMovieAddedUseCase.checkIsMovieInPlaylist(
                                    movieId = state.value.playlistSelectState.movieDetails.id ?: -1,
                                    playlistId = it.playlistId
                                )
                            resultList.add(
                                MappedPlaylist(
                                    playlist = it,
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
                        playlistState.value = resultList
                        postSideEffect(PlaylistSelectSideEffects.UpdatePlaylistSelectList(resultList))
                    }
                }
        }
    }

    fun addMovieToPlaylist(playlistId: Long) = intent {
        addMovieToPlaylistUseCase.insertMovieToPlaylist(
            playlistId = playlistId,
            movie = state.value.playlistSelectState.movieDetails
        )
    }
}