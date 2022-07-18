package com.krykun.domain.repositories.remote

import androidx.paging.PagingData
import com.krykun.domain.model.remote.personcombinedcredits.PersonCombinedCredits
import com.krykun.domain.model.remote.Genre
import com.krykun.domain.model.remote.MovieDiscoverItem
import com.krykun.domain.model.remote.moviecastdetails.CastDetails
import com.krykun.domain.model.remote.moviedetails.MovieDetails
import com.krykun.domain.model.remote.movies.Movie
import com.krykun.domain.model.remote.persondetails.PersonDetails
import com.krykun.domain.model.remote.search.SearchItem
import com.krykun.domain.model.remote.tvcastdetails.TvCastDetails
import com.krykun.domain.model.remote.tvdetails.TvDetails
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