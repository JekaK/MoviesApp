package com.krykun.data.datasource.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.krykun.data.api.ApiService
import com.krykun.data.datasource.MoviesRemoteDataSource
import com.krykun.data.datasource.impl.pagingsource.PopularMoviesPagingSource
import com.krykun.data.datasource.impl.pagingsource.SearchPagingSource
import com.krykun.data.datasource.impl.pagingsource.TopRatedMoviesPagingSource
import com.krykun.data.datasource.impl.pagingsource.TrendingMoviesPagingSource
import com.krykun.data.model.castdetails.CastDetailsResponse
import com.krykun.data.model.genre.Genre
import com.krykun.data.model.moviedetails.MovieDetailsResponse
import com.krykun.data.model.movielistitem.MovieItem
import com.krykun.data.model.movies.MovieItemResponse
import com.krykun.data.model.search.SearchItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesRemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : MoviesRemoteDataSource {

    override fun getUpcomingMovies(
        country: String?,
        language: String?,
        category: String?
    ): Flow<PagingData<MovieItem>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                DiscoverMoviesPagingSource(
                    apiService = apiService,
                )
            }
        ).flow
    }

    override suspend fun getGenres(): Result<List<Genre>> {
        val result = apiService.getGenres()
        return if (result.isSuccessful) {
            Result.success(result.body()?.genres ?: listOf())
        } else {
            Result.failure(Exception(result.errorBody().toString()))
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsResponse> {
        val result = apiService.getMovieDetails(movieId)
        return if (result.isSuccessful) {
            Result.success(result.body()!!)
        } else {
            Result.failure(Exception(result.errorBody().toString()))
        }
    }

    override suspend fun getCastDetails(movieId: Int): Result<CastDetailsResponse> {
        val result = apiService.getCastDetails(movieId)
        return if (result.isSuccessful) {
            Result.success(result.body()!!)
        } else {
            Result.failure(Exception(result.errorBody().toString()))
        }
    }

    override fun getTrendingMovies(): Flow<PagingData<MovieItemResponse>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                TrendingMoviesPagingSource(
                    apiService = apiService,
                )
            }
        ).flow
    }

    override fun getPopularMovies(): Flow<PagingData<MovieItemResponse>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                PopularMoviesPagingSource(
                    apiService = apiService,
                )
            }
        ).flow
    }

    override fun getTopRatedMovies(): Flow<PagingData<MovieItemResponse>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                TopRatedMoviesPagingSource(
                    apiService = apiService,
                )
            }
        ).flow
    }

    override fun makeSearch(query: String): Flow<PagingData<SearchItem>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                SearchPagingSource(
                    query = query,
                    apiService = apiService,
                )
            }
        ).flow
    }
}