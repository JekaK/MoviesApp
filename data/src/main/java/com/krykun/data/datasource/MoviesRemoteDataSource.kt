package com.krykun.data.datasource

import androidx.paging.PagingData
import com.krykun.data.model.Genre
import com.krykun.data.model.MovieItem
import com.krykun.domain.model.MovieDiscoverItem
import kotlinx.coroutines.flow.Flow

interface MoviesRemoteDataSource {

    fun getUpcomingMovies(
        country: String?,
        language: String?,
        category: String?
    ): Flow<PagingData<MovieItem>>

    suspend fun getGenres(): List<Genre>
}