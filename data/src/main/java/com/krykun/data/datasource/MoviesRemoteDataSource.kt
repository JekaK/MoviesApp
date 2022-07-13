package com.krykun.data.datasource

import androidx.paging.PagingData
import com.krykun.data.model.moviecastdetails.CastDetailsResponse
import com.krykun.data.model.genre.Genre
import com.krykun.data.model.moviedetails.MovieDetailsResponse
import com.krykun.data.model.movielistitem.MovieItem
import com.krykun.data.model.movies.MovieItemResponse
import com.krykun.data.model.search.SearchItem
import com.krykun.data.model.tvcastdetails.TvCastDetailsResponse
import com.krykun.data.model.tvdetails.TvDetailsResponse
import com.krykun.domain.model.tvcastdetails.TvCastDetails
import kotlinx.coroutines.flow.Flow

interface MoviesRemoteDataSource {

    fun getUpcomingMovies(
        country: String?,
        language: String?,
        category: String?
    ): Flow<PagingData<MovieItem>>

    suspend fun getGenres(): Result<List<Genre>>

    suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsResponse>

    suspend fun getTvDetails(movieId: Int): Result<TvDetailsResponse>

    suspend fun getMovieCastDetails(movieId: Int): Result<CastDetailsResponse>

    suspend fun getTvCastDetails(movieId: Int): Result<TvCastDetailsResponse>

    fun getTrendingMovies(): Flow<PagingData<MovieItemResponse>>

    fun getPopularMovies(): Flow<PagingData<MovieItemResponse>>

    fun getTopRatedMovies(): Flow<PagingData<MovieItemResponse>>

    fun makeSearch(query: String): Flow<PagingData<SearchItem>>
}