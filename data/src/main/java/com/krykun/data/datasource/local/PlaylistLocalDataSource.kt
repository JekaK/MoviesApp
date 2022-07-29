package com.krykun.data.datasource.local

import com.krykun.data.model.local.Playlist
import com.krykun.data.model.local.PlaylistMovieCrossRef
import com.krykun.data.model.local.PlaylistWithMovies
import kotlinx.coroutines.flow.Flow

interface PlaylistLocalDataSource {

    suspend fun insertPlaylist(playlist: Playlist): Long

    suspend fun insertPlaylistMovieCrossRef(crossRef: PlaylistMovieCrossRef): Long

    fun getPlaylistsWithMoviesFlow(): Flow<List<PlaylistWithMovies>>

    fun getPlaylistsWithMovies(): List<PlaylistWithMovies>

    fun getPlaylistsWithMoviesById(playlistId: Long): Flow<PlaylistWithMovies>

    fun getAllPlaylistsWithMoviesByLimit(amount: Int): Flow<List<PlaylistWithMovies>>

    suspend fun removePlaylist(playlistId: Long)
}