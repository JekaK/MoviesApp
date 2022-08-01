package com.krykun.data.datasource.remote.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.krykun.data.api.ApiService
import com.krykun.data.datasource.remote.MoviesRemoteDataSource
import com.krykun.data.datasource.remote.impl.pagingsource.PopularMoviesPagingSource
import com.krykun.data.datasource.remote.impl.pagingsource.SearchPagingSource
import com.krykun.data.datasource.remote.impl.pagingsource.TopRatedMoviesPagingSource
import com.krykun.data.datasource.remote.impl.pagingsource.TrendingMoviesPagingSource
import com.krykun.data.model.remote.moviecastdetails.CastDetailsResponse
import com.krykun.data.model.remote.genre.Genre
import com.krykun.data.model.remote.moviedetails.MovieDetailsResponse
import com.krykun.data.model.remote.movielistitem.MovieItem
import com.krykun.data.model.remote.movies.MovieItemResponse
import com.krykun.data.model.remote.personcombinedcredits.PersonCombinedCreditsResponse
import com.krykun.data.model.remote.persondetails.PersonDetailsResponse
import com.krykun.data.model.remote.search.SearchItem
import com.krykun.data.model.remote.tvcastdetails.TvCastDetailsResponse
import com.krykun.data.model.remote.tvdetails.TvDetailsResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesRemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : MoviesRemoteDataSource {

    /**
     * > We're creating a pager that will use the `DiscoverMoviesPagingSourceImpl` to fetch data from
     * the API
     *
     * @param country The ISO 3166-1 code of the country to get the movies for.
     * @param language The language to include.
     * @param category The category of movies to be fetched.
     * @return A Flow of PagingData of MovieItem
     */
    override fun getUpcomingMovies(
        country: String?,
        language: String?,
        category: String?
    ): Flow<PagingData<MovieItem>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                DiscoverMoviesPagingSourceImpl(
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

    override suspend fun getTvGenres(): Result<List<Genre>> {
        val result = apiService.getTvGenres()
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

    override suspend fun getTvDetails(movieId: Int): Result<TvDetailsResponse> {
        val result = apiService.getTvDetails(movieId)
        return if (result.isSuccessful) {
            Result.success(result.body()!!)
        } else {
            Result.failure(Exception(result.errorBody().toString()))
        }
    }

    override suspend fun getMovieCastDetails(movieId: Int): Result<CastDetailsResponse> {
        val result = apiService.getMovieCastDetails(movieId)
        return if (result.isSuccessful) {
            Result.success(result.body()!!)
        } else {
            Result.failure(Exception(result.errorBody().toString()))
        }
    }

    override suspend fun getTvCastDetails(movieId: Int): Result<TvCastDetailsResponse> {
        val result = apiService.getTvCastDetails(movieId)
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

    override suspend fun getPersonDetails(movieId: Int): Result<PersonDetailsResponse> {
        val result = apiService.getPersonDetails(movieId)
        return if (result.isSuccessful) {
            Result.success(result.body() ?: PersonDetailsResponse())
        } else {
            Result.failure(Exception(result.errorBody().toString()))
        }
    }

    override suspend fun getPersonCombinedCredits(personId: Int): Result<PersonCombinedCreditsResponse> {
        val result = apiService.getPersonCombinedCredits(personId)
        return if (result.isSuccessful) {
            Result.success(result.body() ?: PersonCombinedCreditsResponse())
        } else {
            Result.failure(Exception(result.errorBody().toString()))
        }
    }
}