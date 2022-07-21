package com.krykun.movieapp.feature.playlist.details.presentation

import com.krykun.domain.model.local.Playlist

data class PlaylistDetailsState(
    val playlistId: Long = -1,
    val playlist: Playlist? = null
)
