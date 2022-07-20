package com.krykun.data.repo

import com.krykun.data.datasource.local.MoviesLocalDataSource
import com.krykun.data.mappers.local.MoviesMapper.toMovie
import com.krykun.domain.model.remote.moviedetails.MovieDetails
import com.krykun.domain.model.remote.tvdetails.TvDetails
import com.krykun.domain.repositories.local.MoviesLocalRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesLocalRepoImpl @Inject constructor(
    private val moviesLocalDataSource: MoviesLocalDataSource,
) : MoviesLocalRepo {

    override suspend fun insertMovie(movie: MovieDetails, playlistId: Long): Long =
        moviesLocalDataSource.insertMovie(movie.toMovie(), playlistId)


    override suspend fun insertMovie(movie: TvDetails, playlistId: Long): Long =
        moviesLocalDataSource.insertMovie(movie.toMovie(), playlistId)


    override fun removeMovieFromPlaylist(movie: MovieDetails, playlistId: Long) {
        moviesLocalDataSource.removeMovieFromPlaylist(movie.toMovie(), playlistId)
    }

    override fun removeMovieFromPlaylist(movie: TvDetails, playlistId: Long) {
        moviesLocalDataSource.removeMovieFromPlaylist(movie.toMovie(), playlistId)
    }

    override fun isMovieAdded(movieId: Int): Flow<Boolean> {
        return moviesLocalDataSource.isMovieAdded(movieId)
    }
}