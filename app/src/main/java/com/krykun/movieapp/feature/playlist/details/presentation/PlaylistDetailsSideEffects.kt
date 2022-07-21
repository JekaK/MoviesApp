package com.krykun.movieapp.feature.playlist.details.presentation

import com.krykun.domain.model.local.Playlist

sealed class PlaylistDetailsSideEffects {

    data class UpdatePlaylistInfo(val playlist: Playlist) : PlaylistDetailsSideEffects()

    object NavigateToMovieDetails : PlaylistDetailsSideEffects()
    object NavigateToTvDetails : PlaylistDetailsSideEffects()
}