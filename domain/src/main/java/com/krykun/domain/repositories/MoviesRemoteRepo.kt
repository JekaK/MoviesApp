package com.krykun.domain.repositories

import androidx.paging.PagingData
import com.krykun.domain.model.Genre
import com.krykun.domain.model.MovieDiscoverItem
import com.krykun.domain.model.castdetails.CastDetails
import com.krykun.domain.model.moviedetails.MovieDetails
import kotlinx.coroutines.flow.Flow

interface MoviesRemoteRepo {
    fun getUpcomingMovies(
        country: String?,
        language: String?,
        category: String?,
        genres: List<Genre>
    ): Flow<PagingData<MovieDiscoverItem>>

    suspend fun getGenres(): List<Genre>

    suspend fun getMovieDetails(movieId: Int): MovieDetails

    suspend fun getCastDetails(movieId: Int): CastDetails
}