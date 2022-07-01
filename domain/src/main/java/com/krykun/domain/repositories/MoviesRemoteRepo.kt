package com.krykun.domain.repositories

import androidx.paging.PagingData
import com.krykun.domain.model.Genre
import com.krykun.domain.model.MovieDiscoverItem
import kotlinx.coroutines.flow.Flow

interface MoviesRemoteRepo {
    fun getUpcomingMovies(
        country: String?,
        language: String?,
        category: String?,
        genres: List<Genre>
    ): Flow<PagingData<MovieDiscoverItem>>

    suspend fun getGenres(): List<Genre>
}