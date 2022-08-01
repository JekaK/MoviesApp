package com.krykun.data.datasource.remote.impl.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.krykun.data.api.ApiService
import com.krykun.data.model.remote.search.SearchItem

/* It's a PagingSource that loads data from the API, and it's refresh key is the next page number */
class SearchPagingSource(
    private val query: String,
    private val apiService: ApiService,
) : PagingSource<Int, SearchItem>() {

    /**
     * > We're trying to load the next page of data, and if we succeed, we return a `LoadResult.Page`
     * object with the data, the previous page key (which is always null in our case), and the next
     * page key (which is the current page number + 1, unless we're on the last page, in which case
     * it's null). If we fail, we return a `LoadResult.Error` object with the exception
     *
     * @param params LoadParams<Int> - This is the page number that we want to load.
     * @return LoadResult.Page(
     *                 data = response.results as List<SearchItem>,
     *                 prevKey = null,
     *                 nextKey = when {
     *                     (response.page + 1) <= response.totalPages -> {
     *                         response.page + 1
     *                     }
     *                     else -> null
     *                 }
     *             )
     */
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

    /**
     * If the anchor position is not null, then get the closest page to the anchor position, and if the
     * previous key is not null, then add 1 to it, otherwise, if the next key is not null, then
     * subtract 1 from it
     *
     * @param state PagingState<Int, SearchItem>
     * @return The key of the next page to load.
     */
    override fun getRefreshKey(state: PagingState<Int, SearchItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}