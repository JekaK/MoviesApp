package com.krykun.domain.repositories

import androidx.paging.PagingData
import com.krykun.domain.model.MovieDiscoverItem
import kotlinx.coroutines.flow.Flow

interface MoviesRemoteRepo {
    fun getUpcomingMovies(
        country: String?,
        language: String?,
        category: String?
    ): Flow<PagingData<MovieDiscoverItem>>
}