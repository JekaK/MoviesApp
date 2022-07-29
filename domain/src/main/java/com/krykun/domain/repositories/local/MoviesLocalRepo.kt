package com.krykun.domain.repositories.local

import com.krykun.domain.model.remote.moviedetails.MovieDetails
import com.krykun.domain.model.remote.tvdetails.TvDetails
import kotlinx.coroutines.flow.Flow

interface MoviesLocalRepo {
    suspend fun insertMovie(movie: MovieDetails, playlistId: Long): Long
    suspend fun insertMovie(movie: TvDetails, playlistId: Long): Long
    suspend fun removeMovieFromPlaylist(movie: MovieDetails, playlistId: Long)
    suspend fun removeMovieFromPlaylist(movie: TvDetails, playlistId: Long)
    fun isMovieAdded(movieId: Int, playlistId: Int): Boolean
}