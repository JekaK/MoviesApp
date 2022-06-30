package com.krykun.data.datasource.impl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.krykun.data.api.ApiService
import com.krykun.data.datasource.MoviesRemoteDataSource
import com.krykun.data.model.MovieItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MoviesRemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : MoviesRemoteDataSource {

    override fun getDiscoverMovies(
        country: String?,
        language: String?,
        category: String?
    ): Flow<PagingData<MovieItem>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                MoviesPagingSource(
                    apiService = apiService,
                )
            }
        ).flow
    }
}