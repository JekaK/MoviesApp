package com.krykun.movieapp.feature.playlist.main.presentation

import com.krykun.domain.model.local.Playlist

sealed class PlaylistSideEffects {
    object NavigateToPlaylistDetails : PlaylistSideEffects()
    data class UpdatePlaylist(val playlist: List<Playlist>) : PlaylistSideEffects()
}