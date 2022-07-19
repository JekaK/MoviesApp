package com.krykun.movieapp.feature.playlist.presentation

import com.krykun.domain.usecase.local.GetAllPlaylistsUseCase
import com.krykun.domain.usecase.remote.discover.GetDiscoverMoviesUseCase
import com.krykun.movieapp.base.BaseViewModel
import com.krykun.movieapp.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    appState: MutableStateFlow<AppState>,
    getAllPlaylistsUseCase: GetAllPlaylistsUseCase
) : BaseViewModel<PlaylistSideEffects>(appState) {

    val allPlaylists = getAllPlaylistsUseCase.getAllPlaylists()
}