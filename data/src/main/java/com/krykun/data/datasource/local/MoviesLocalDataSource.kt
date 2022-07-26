package com.krykun.data.datasource.local

import com.krykun.data.model.local.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesLocalDataSource {
    suspend fun insertMovie(movie: Movie, playlistId: Long): Long
    fun removeMovieFromPlaylist(movie: Movie, playlistId: Long)
    fun isMovieAdded(movieId: Int, playlistId: Int): Boolean
}