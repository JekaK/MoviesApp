package com.krykun.data.dao

import androidx.room.*
import com.krykun.data.model.local.Movie
import com.krykun.data.model.local.PlaylistMovieCrossRef

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaylistMovieCrossRef(crossRef: PlaylistMovieCrossRef)

    @Delete
    fun removePlaylistMovieCrossRef(crossRef: PlaylistMovieCrossRef)

    @Transaction
    @Delete
    fun removeMovieFromPlaylist(movie: Movie)
}