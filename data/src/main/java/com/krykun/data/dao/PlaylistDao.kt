package com.krykun.data.dao

import androidx.room.*
import com.krykun.data.model.local.Movie
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
    fun getAllPlaylistsWithMoviesFlow(): Flow<List<PlaylistWithMovies>>

    @Transaction
    @Query("SELECT * FROM Playlist")
    fun getAllPlaylistsWithMovies(): List<PlaylistWithMovies>

    @Transaction
    @Query("SELECT * FROM Playlist WHERE playlistId==:playlistId")
    fun getPlaylistsWithMoviesById(playlistId: Long): Flow<PlaylistWithMovies>

    @Transaction
    @Query("select * from Movie inner join PlaylistMovieCrossRef on Movie.movieId = PlaylistMovieCrossRef.movieId inner join Playlist on Playlist.playlistId = PlaylistMovieCrossRef.playlistId WHERE  Playlist.playlistId==1 LIMIT :amount")
    fun getAllPlaylistsWithMoviesByLimit(amount: Int): Flow<List<PlaylistWithMovies>>

    @Transaction
    @Query("DELETE FROM Playlist WHERE playlistId = :playlistId")
    suspend fun removePlaylist(playlistId: Long)

    @Query("SELECT * FROM PlaylistMovieCrossRef WHERE playlistId = :playlistId")
    suspend fun searchInCrossRefForPlaylist(playlistId: Long): List<PlaylistMovieCrossRef>

    @Query("DELETE FROM PLAYLISTMOVIECROSSREF WHERE playlistId = :playlistId")
    suspend fun deleteAllPlaylistCrossRefById(playlistId: Long)
}