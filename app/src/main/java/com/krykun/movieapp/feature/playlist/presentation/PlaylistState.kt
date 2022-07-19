package com.krykun.movieapp.feature.playlist.presentation

import com.krykun.domain.model.local.Playlist

data class PlaylistState(
    val playlists: List<Playlist> = listOf()
)