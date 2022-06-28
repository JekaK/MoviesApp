package com.krykun.domain.repositories

import androidx.paging.PagingData
import com.krykun.domain.model.MovieDiscoverItem
import kotlinx.coroutines.flow.Flow

interface MoviesRemoteRepo {
    fun getDiscoverMovies(
        country: String? = null,
        language: String? = null,
        category: String? = null
    ): Flow<PagingData<MovieDiscoverItem>>
}