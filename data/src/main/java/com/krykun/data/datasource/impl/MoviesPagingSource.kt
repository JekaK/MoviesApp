package com.krykun.data.datasource.impl

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.krykun.data.api.ApiService
import com.krykun.data.model.movielistitem.MovieItem

class MoviesPagingSource(
    private val apiService: ApiService,
) : PagingSource<Int, MovieItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItem> {
        val nextPageNumber = params.key ?: 1
        val response = apiService.getDiscoverMovies(
            page = nextPageNumber,
        )
        return LoadResult.Page(
            data = response.results as List<MovieItem>,
            prevKey = null,
            nextKey = when {
                (response.page + 1) <= response.totalPages -> {
                    response.page + 1
                }
                else -> null
            }
        )
    }

    override fun getRefreshKey(state: PagingState<Int, MovieItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}