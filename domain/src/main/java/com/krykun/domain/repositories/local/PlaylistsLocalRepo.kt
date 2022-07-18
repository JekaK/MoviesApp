package com.krykun.domain.repositories.local

import com.krykun.domain.model.local.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsLocalRepo {

    fun insertPlaylist(playlist: Playlist)

    fun getAllPlaylists(): Flow<List<Playlist>>

    fun getPlaylistById(playlistId: Long): Flow<Playlist>
}