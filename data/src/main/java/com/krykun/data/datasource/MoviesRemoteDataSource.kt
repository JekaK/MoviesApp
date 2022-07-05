package com.krykun.data.datasource

import androidx.paging.PagingData
import com.krykun.data.model.genre.Genre
import com.krykun.data.model.moviedetails.MovieDetailsResponse
import com.krykun.data.model.movielistitem.MovieItem
import kotlinx.coroutines.flow.Flow

interface MoviesRemoteDataSource {

    fun getUpcomingMovies(
        country: String?,
        language: String?,
        category: String?
    ): Flow<PagingData<MovieItem>>

    suspend fun getGenres(): List<Genre>

    suspend fun getMovieDetails(movieId: Int): MovieDetailsResponse
}