package com.krykun.movieapp.feature.playlistselect.presentation

sealed class PlaylistSelectSideEffects {
    data class UpdatePlaylistSelectList(
        val playlists: List<MappedPlaylist>
    ) : PlaylistSelectSideEffects()
}
