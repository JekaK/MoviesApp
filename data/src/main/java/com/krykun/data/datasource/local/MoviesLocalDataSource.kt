package com.krykun.data.datasource.local

import com.krykun.data.model.local.Movie

interface MoviesLocalDataSource {
    suspend fun insertMovie(movie: Movie, playlistId: Long): Long
    fun removeMovieFromPlaylist(movie: Movie, playlistId: Long)
}