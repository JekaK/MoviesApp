package com.krykun.data.dao

import androidx.room.*
import com.krykun.data.model.local.Movie
import com.krykun.data.model.local.PlaylistMovieCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylistMovieCrossRef(crossRef: PlaylistMovieCrossRef): Long

    @Delete
    fun removePlaylistMovieCrossRef(crossRef: PlaylistMovieCrossRef)

    @Transaction
    @Delete
    fun removeMovieFromPlaylist(movie: Movie)

    @Query("SELECT EXISTS(SELECT 1 FROM Movie WHERE movieId = :movieId LIMIT 1)")
    fun isAddedToPlaylist(movieId: Int): Flow<Boolean>
}