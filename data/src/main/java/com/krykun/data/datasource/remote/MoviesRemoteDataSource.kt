package com.krykun.data.datasource.remote

import androidx.paging.PagingData
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

interface MoviesRemoteDataSource {

    fun getUpcomingMovies(
        country: String?,
        language: String?,
        category: String?
    ): Flow<PagingData<MovieItem>>

    suspend fun getGenres(): Result<List<Genre>>

    suspend fun getTvGenres(): Result<List<Genre>>

    suspend fun getMovieDetails(movieId: Int): Result<MovieDetailsResponse>

    suspend fun getTvDetails(movieId: Int): Result<TvDetailsResponse>

    suspend fun getMovieCastDetails(movieId: Int): Result<CastDetailsResponse>

    suspend fun getTvCastDetails(movieId: Int): Result<TvCastDetailsResponse>

    fun getTrendingMovies(): Flow<PagingData<MovieItemResponse>>

    fun getPopularMovies(): Flow<PagingData<MovieItemResponse>>

    fun getTopRatedMovies(): Flow<PagingData<MovieItemResponse>>

    fun makeSearch(query: String): Flow<PagingData<SearchItem>>

    suspend fun getPersonDetails(movieId: Int): Result<PersonDetailsResponse>

    suspend fun getPersonCombinedCredits(personId: Int): Result<PersonCombinedCreditsResponse>

    fun getMovieRecommendations(
        movieId: Int
    ): Flow<PagingData<MovieRecommendationResponse>>
}