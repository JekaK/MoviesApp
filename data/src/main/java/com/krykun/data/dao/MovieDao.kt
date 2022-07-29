package com.krykun.data.dao

import androidx.room.*
import com.krykun.data.model.local.Movie
import com.krykun.data.model.local.PlaylistMovieCrossRef

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylistMovieCrossRef(crossRef: PlaylistMovieCrossRef): Long

    @Transaction
    @Delete
    fun removePlaylistMovieCrossRef(crossRef: PlaylistMovieCrossRef)

    @Query("SELECT EXISTS(SELECT 1 FROM PlaylistMovieCrossRef WHERE movieId = :movieId LIMIT 1)")
    suspend fun searchInCrossRefForMovie(movieId: Int): Long

    @Query("SELECT * FROM MOVIE")
    suspend fun getAllMovies(): List<Movie>

    @Transaction
    @Delete
    fun removeMovieFromPlaylist(movie: Movie)

    @Transaction
    @Query("DELETE FROM MOVIE WHERE movieId = :movieId")
    suspend fun removeMovieFromPlaylistById(movieId: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM PlaylistMovieCrossRef WHERE movieId = :movieId AND playlistId = :playlistId LIMIT 1)")
    fun isAddedToPlaylist(movieId: Int, playlistId: Int): Boolean
}