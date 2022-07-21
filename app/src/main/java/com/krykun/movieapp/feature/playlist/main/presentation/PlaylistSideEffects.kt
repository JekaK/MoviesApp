package com.krykun.movieapp.feature.playlist.main.presentation

sealed class PlaylistSideEffects {
    object NavigateToPlaylistDetails : PlaylistSideEffects()
}