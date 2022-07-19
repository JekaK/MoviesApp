package com.krykun.domain.repositories.local

import com.krykun.domain.model.local.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsLocalRepo {

    suspend fun insertPlaylist(playlist: Playlist): Long

    fun getAllPlaylists(): Flow<List<Playlist>>

    fun getPlaylistById(playlistId: Long): Flow<Playlist>
}