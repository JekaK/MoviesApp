package com.krykun.data.dao

import androidx.room.*
import com.krykun.data.model.local.Playlist
import com.krykun.data.model.local.PlaylistMovieCrossRef
import com.krykun.data.model.local.PlaylistWithMovies
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: Playlist): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylistMovieCrossRef(crossRef: PlaylistMovieCrossRef): Long

    @Transaction
    @Query("SELECT * FROM Playlist")
    fun getPlaylistsWithMovies(): Flow<List<PlaylistWithMovies>>

    @Transaction
    @Query("SELECT * FROM Playlist WHERE playlistId==:playlistId")
    fun getPlaylistsWithMoviesById(playlistId: Long): Flow<PlaylistWithMovies>
}