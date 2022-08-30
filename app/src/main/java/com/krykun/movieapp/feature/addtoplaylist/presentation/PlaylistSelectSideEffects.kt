package com.krykun.movieapp.feature.addtoplaylist.presentation

sealed class PlaylistSelectSideEffects {
    data class UpdatePlaylistSelectList(
        val playlists: List<MappedPlaylist>
    ) : PlaylistSelectSideEffects()
}
