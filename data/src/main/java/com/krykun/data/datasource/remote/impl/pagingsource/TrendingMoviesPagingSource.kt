package com.krykun.data.datasource.remote.impl.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.krykun.data.api.ApiService
import com.krykun.data.model.remote.movies.MovieItemResponse

class TrendingMoviesPagingSource(
    private val apiService: ApiService,
) : PagingSource<Int, MovieItemResponse>() {

    /**
     * We are trying to load the next page of data from the API, and if we get a response, we return a
     * LoadResult.Page object with the data, the previous key (which is null in this case), and the
     * next key (which is the next page number). If we get an error, we return a LoadResult.Error
     * object with the error
     *
     * @param params LoadParams<Int> - This is the page number that we want to load.
     * @return LoadResult.Page(
     *                 data = response.results as List<MovieItemResponse>,
     *                 prevKey = null,
     *                 nextKey = when {
     *                     (response.page + 1) <= response.totalPages -> {
     *                         response.page + 1
     *                     }
     *                     else -> null
     *                 }
     *             )
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieItemResponse> {
        try {
            val nextPageNumber = params.key ?: 1
            val response = apiService.getTrendingMovies(
                page = nextPageNumber,
            )
            return LoadResult.Page(
                data = response.results as List<MovieItemResponse>,
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

    override fun getRefreshKey(state: PagingState<Int, MovieItemResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}