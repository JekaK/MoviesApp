package com.krykun.data.datasource.local.impl

import com.krykun.data.dao.MovieDao
import com.krykun.data.datasource.local.MoviesLocalDataSource
import com.krykun.data.model.local.Movie
import com.krykun.data.model.local.PlaylistMovieCrossRef

class MoviesLocalDataSourceImpl(private val movieDao: MovieDao) :
    MoviesLocalDataSource {

    override suspend fun insertMovie(movie: Movie, playlistId: Long): Long {
        val movieInsert = movieDao.insertMovie(movie)
        movieDao.insertPlaylistMovieCrossRef(
            PlaylistMovieCrossRef(
                playlistId = playlistId,
                movieId = movie.movieId
            )
        )
        return movieInsert
    }

    override fun removeMovieFromPlaylist(movie: Movie, playlistId: Long) {
        movieDao.removeMovieFromPlaylist(movie)
        movieDao.removePlaylistMovieCrossRef(
            PlaylistMovieCrossRef(
                playlistId = playlistId,
                movieId = movie.movieId
            )
        )
    }

    override fun isMovieAdded(movieId: Int, playlistId: Int): Boolean {
        return movieDao.isAddedToPlaylist(
            movieId = movieId,
            playlistId = playlistId
        )
    }
}