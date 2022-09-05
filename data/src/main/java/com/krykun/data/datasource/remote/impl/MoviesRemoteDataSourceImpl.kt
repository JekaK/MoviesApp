package com.krykun.data.datasource.remote.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.krykun.data.api.ApiService
import com.krykun.data.datasource.remote.MoviesRemoteDataSource
import com.krykun.data.datasource.remote.impl.pagingsource.*
import com.krykun.data.model.remote.moviecastdetails.CastDetailsResponse
import com.krykun.data.model.remote.genre.Genre
import com.krykun.data.model.remote.moviedetails.MovieDetailsResponse
import com.krykun.data.model.remote.movielistitem.MovieItem
import com.krykun.data.model.remote.movierecommendations.MovieRecommendationResponse
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

    /**
     * We're calling the API service, and if the result is successful, we're returning a success result
     * with the list of genres, otherwise we're returning a failure result with an exception
     *
     * @return Result<List<Genre>>
     */
    override suspend fun getGenres(): Result<List<Genre>> {
        val result = apiService.getGenres()
        return if (result.isSuccessful) {
            Result.success(result.body()?.genres ?: listOf())
        } else {
            Result.failure(Exception(result.errorBody().toString()))
        }
    }

    /**
     * We're calling the API service, and if the result is successful, we're returning a success result
     * with the list of genres, otherwise we're returning a failure result with an exception
     *
     * @return Result<List<Genre>>
     */
    override suspend fun getTvGenres(): Result<List<Genre>> {
        val result = apiService.getTvGenres()
        return if (result.isSuccessful) {
            Result.success(result.body()?.genres ?: listOf())
        } else {
            Result.failure(Exception(result.errorBody().toString()))
        }
    }

    /**
     * It fetches the movie details from the API.
     *
     * @param movieId The id of the movie you want to get details for.
     * @return Result<MovieDetailsResponse>
     */
    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsResponse> {
        val result = apiService.getMovieDetails(movieId)
        return if (result.isSuccessful) {
            Result.success(result.body()!!)
        } else {
            Result.failure(Exception(result.errorBody().toString()))
        }
    }

    /**
     * It's a suspend function that returns a Result object
     *
     * @param movieId The id of the movie you want to get details for.
     * @return Result<TvDetailsResponse>
     */
    override suspend fun getTvDetails(movieId: Int): Result<TvDetailsResponse> {
        val result = apiService.getTvDetails(movieId)
        return if (result.isSuccessful) {
            Result.success(result.body()!!)
        } else {
            Result.failure(Exception(result.errorBody().toString()))
        }
    }

    /**
     * It makes a network call to the API, and returns the result of the network call as a Result
     * object
     *
     * @param movieId The id of the movie whose cast details are to be fetched.
     * @return Result<CastDetailsResponse>
     */
    override suspend fun getMovieCastDetails(movieId: Int): Result<CastDetailsResponse> {
        val result = apiService.getMovieCastDetails(movieId)
        return if (result.isSuccessful) {
            Result.success(result.body()!!)
        } else {
            Result.failure(Exception(result.errorBody().toString()))
        }
    }

    /**
     * It fetches the cast details of a TV show.
     *
     * @param movieId The id of the movie you want to get the cast details for.
     * @return Result<TvCastDetailsResponse>
     */
    override suspend fun getTvCastDetails(movieId: Int): Result<TvCastDetailsResponse> {
        val result = apiService.getTvCastDetails(movieId)
        return if (result.isSuccessful) {
            Result.success(result.body()!!)
        } else {
            Result.failure(Exception(result.errorBody().toString()))
        }
    }

    /**
     * We are creating a Pager object with a PagingConfig object that has a pageSize of 20, and a
     * pagingSourceFactory that creates a TrendingMoviesPagingSource object that uses the apiService to
     * fetch data
     *
     * @return A Flow of PagingData of MovieItemResponse
     */
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

    /**
     * We are creating a pager with a page size of 20, and the paging source factory is a
     * PopularMoviesPagingSource which is a class that we will create in the next step
     *
     * @return A Flow of PagingData of MovieItemResponse
     */
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

    /**
     * We are creating a Pager object with a PagingConfig object that has a pageSize of 20, and a
     * pagingSourceFactory that creates a TopRatedMoviesPagingSource object that uses the apiService to
     * get the data
     *
     * @return A Flow of PagingData of MovieItemResponse
     */
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

    /**
     * > We create a Pager object with a PagingConfig object that specifies the page size, and a paging
     * source factory that creates a SearchPagingSource object
     *
     * @param query The search query
     * @return A Flow of PagingData of SearchItem
     */
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

    /**
     * It fetches the person details from the API.
     *
     * @param movieId The id of the movie you want to get the details of.
     * @return Result<PersonDetailsResponse>
     */
    override suspend fun getPersonDetails(movieId: Int): Result<PersonDetailsResponse> {
        val result = apiService.getPersonDetails(movieId)
        return if (result.isSuccessful) {
            Result.success(result.body() ?: PersonDetailsResponse())
        } else {
            Result.failure(Exception(result.errorBody().toString()))
        }
    }

    /**
     * `getPersonCombinedCredits` is a function that takes an integer as a parameter and returns a
     * Result object that contains either a PersonCombinedCreditsResponse object or an Exception object
     *
     * @param personId The id of the person you want to get the credits for.
     * @return Result<PersonCombinedCreditsResponse>
     */
    override suspend fun getPersonCombinedCredits(personId: Int): Result<PersonCombinedCreditsResponse> {
        val result = apiService.getPersonCombinedCredits(personId)
        return if (result.isSuccessful) {
            Result.success(result.body() ?: PersonCombinedCreditsResponse())
        } else {
            Result.failure(Exception(result.errorBody().toString()))
        }
    }

    override fun getMovieRecommendations(
        movieId: Int
    ): Flow<PagingData<MovieRecommendationResponse>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                RecommendationMoviesPagingSourceImpl(
                    apiService = apiService,
                    movieId = movieId
                )
            }
        ).flow
    }
}