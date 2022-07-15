package com.krykun.domain.repositories

import androidx.paging.PagingData
import com.krykun.domain.model.personcombinedcredits.PersonCombinedCredits
import com.krykun.domain.model.Genre
import com.krykun.domain.model.MovieDiscoverItem
import com.krykun.domain.model.moviecastdetails.CastDetails
import com.krykun.domain.model.moviedetails.MovieDetails
import com.krykun.domain.model.movies.Movie
import com.krykun.domain.model.persondetails.PersonDetails
import com.krykun.domain.model.search.SearchItem
import com.krykun.domain.model.tvcastdetails.TvCastDetails
import com.krykun.domain.model.tvdetails.TvDetails
import kotlinx.coroutines.flow.Flow

interface MoviesRemoteRepo {
    fun getDiscoverMovies(
        country: String?,
        language: String?,
        category: String?,
        genres: List<Genre>
    ): Flow<PagingData<MovieDiscoverItem>>

    suspend fun getGenres(): Result<List<Genre>>

    suspend fun getTvGenres(): Result<List<Genre>>

    suspend fun getMovieDetails(movieId: Int): Result<MovieDetails>

    suspend fun getTvDetails(tvId: Int): Result<TvDetails>

    suspend fun getMovieCastDetails(movieId: Int): Result<CastDetails>

    suspend fun getTvSeriesCastDetail(tvId: Int): Result<TvCastDetails>

    fun getTrendingMovies(
        genres: List<Genre>,
    ): Flow<PagingData<Movie>>

    fun getPopularMovies(
        genres: List<Genre>,
    ): Flow<PagingData<Movie>>

    fun getTopRatedMovies(
        genres: List<Genre>,
    ): Flow<PagingData<Movie>>

    fun makeSearch(
        query: String,
        genres: List<Genre>,
    ): Flow<PagingData<SearchItem>>

    suspend fun getPersonDetails(personId: Int): Result<PersonDetails>

    suspend fun getPersonCombinedCredits(
        personId: Int,
        genres: List<Genre>
    ): Result<PersonCombinedCredits>

}