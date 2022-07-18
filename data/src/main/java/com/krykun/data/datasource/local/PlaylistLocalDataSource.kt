package com.krykun.data.datasource.local

import com.krykun.data.model.local.Playlist
import com.krykun.data.model.local.PlaylistMovieCrossRef
import com.krykun.data.model.local.PlaylistWithMovies
import kotlinx.coroutines.flow.Flow

interface PlaylistLocalDataSource {

    fun insertPlaylist(playlist: Playlist)

    fun insertPlaylistMovieCrossRef(crossRef: PlaylistMovieCrossRef)

    fun getPlaylistsWithMovies(): Flow<List<PlaylistWithMovies>>

    fun getPlaylistsWithMoviesById(playlistId: Long): Flow<PlaylistWithMovies>
}