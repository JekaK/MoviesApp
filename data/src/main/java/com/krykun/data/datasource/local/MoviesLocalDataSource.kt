package com.krykun.data.datasource.local

import com.krykun.data.model.local.Movie

interface MoviesLocalDataSource {
    fun insertMovie(movie: Movie, playlistId: Long)
    fun removeMovieFromPlaylist(movie: Movie, playlistId: Long)
}