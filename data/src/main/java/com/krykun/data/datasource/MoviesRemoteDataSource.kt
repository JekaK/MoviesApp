package com.krykun.data.datasource

import androidx.paging.PagingData
import com.krykun.data.model.MovieItem
import com.krykun.domain.model.MovieDiscoverItem
import kotlinx.coroutines.flow.Flow

interface MoviesRemoteDataSource {

    fun getDiscoverMovies(
        country: String?,
        language: String?,
        category: String?
    ): Flow<PagingData<MovieItem>>
}