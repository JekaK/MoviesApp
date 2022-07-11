package com.krykun.domain.repositories

import androidx.paging.PagingData
import com.krykun.domain.model.Genre
import com.krykun.domain.model.MovieDiscoverItem
import com.krykun.domain.model.castdetails.CastDetails
import com.krykun.domain.model.moviedetails.MovieDetails
import com.krykun.domain.model.movies.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesRemoteRepo {
    fun getDiscoverMovies(
        country: String?,
        language: String?,
        category: String?,
        genres: List<Genre>
    ): Flow<PagingData<MovieDiscoverItem>>

    suspend fun getGenres(): Result<List<Genre>>

    suspend fun getMovieDetails(movieId: Int): Result<MovieDetails>

    suspend fun getCastDetails(movieId: Int): Result<CastDetails>

    fun getTrendingMovies(
        genres: List<Genre>,
    ): Flow<PagingData<Movie>>

    fun getPopularMovies(
        genres: List<Genre>,
    ): Flow<PagingData<Movie>>

    fun getTopRatedMovies(
        genres: List<Genre>,
    ): Flow<PagingData<Movie>>
}