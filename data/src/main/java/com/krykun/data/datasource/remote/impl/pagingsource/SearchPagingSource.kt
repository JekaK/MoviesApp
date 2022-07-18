package com.krykun.data.datasource.remote.impl.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.krykun.data.api.ApiService
import com.krykun.data.model.remote.search.SearchItem

class SearchPagingSource(
    private val query: String,
    private val apiService: ApiService,
) : PagingSource<Int, SearchItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchItem> {
        try {
            val nextPageNumber = params.key ?: 1
            val response = apiService.search(
                query = query,
                page = nextPageNumber,
            )
            return LoadResult.Page(
                data = response.results as List<SearchItem>,
                prevKey = null,
                nextKey = when {
                    (response.page + 1) <= response.totalPages -> {
                        response.page + 1
                    }
                    else -> null
                }
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, SearchItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}