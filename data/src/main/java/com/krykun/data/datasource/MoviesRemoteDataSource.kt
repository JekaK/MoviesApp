package com.krykun.data.datasource

import androidx.paging.PagingData
import com.krykun.data.model.castdetails.CastDetailsResponse
import com.krykun.data.model.genre.Genre
import com.krykun.data.model.moviedetails.MovieDetailsResponse
import com.krykun.data.model.movielistitem.MovieItem
import com.krykun.data.model.movies.MovieItemResponse
import kotlinx.coroutines.flow.Flow

interface MoviesRemoteDataSource {

    fun getUpcomingMovies(
        country: String?,
        language: String?,
        category: String?
    ): Flow<PagingData<MovieItem>>

    suspend fun getGenres(): Result<List<Genre>>

    suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsResponse>

    suspend fun getCastDetails(movieId: Int): Result<CastDetailsResponse>

    fun getTrendingMovies(): Flow<PagingData<MovieItemResponse>>

    fun getPopularMovies(): Flow<PagingData<MovieItemResponse>>

    fun getTopRatedMovies(): Flow<PagingData<MovieItemResponse>>
}