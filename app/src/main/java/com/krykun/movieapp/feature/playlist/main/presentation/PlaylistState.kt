package com.krykun.movieapp.feature.playlist.main.presentation

import com.krykun.domain.model.local.Playlist
import com.krykun.movieapp.feature.playlist.details.presentation.PlaylistDetailsState

data class PlaylistState(
    val playlists: List<Playlist> = listOf(),
    val playlistDetailsState: PlaylistDetailsState = PlaylistDetailsState()
)