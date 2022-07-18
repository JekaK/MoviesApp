package com.krykun.data.dao

import androidx.room.*
import com.krykun.data.model.local.Playlist
import com.krykun.data.model.local.PlaylistMovieCrossRef
import com.krykun.data.model.local.PlaylistWithMovies
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylist(playlist: Playlist)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylistMovieCrossRef(crossRef: PlaylistMovieCrossRef)

    @Transaction
    @Query("SELECT * FROM Playlist")
    fun getPlaylistsWithMovies(): Flow<List<PlaylistWithMovies>>

    @Transaction
    @Query("SELECT * FROM Playlist WHERE playlistId==:playlistId")
    fun getPlaylistsWithMoviesById(playlistId: Long): Flow<PlaylistWithMovies>
}