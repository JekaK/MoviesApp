package com.krykun.domain.repositories.local

import com.krykun.domain.model.local.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsLocalRepo {

    suspend fun insertPlaylist(playlist: Playlist): Long

    suspend fun removePlaylist(playlistId: Long)

    fun getAllPlaylistsFlow(): Flow<List<Playlist>>

    fun getAllPlaylists(): List<Playlist>

    fun getPlaylistById(playlistId: Long): Flow<Playlist>

    fun getAllPlaylistsWithMoviesByLimit(amount: Int): Flow<List<Playlist>>
}