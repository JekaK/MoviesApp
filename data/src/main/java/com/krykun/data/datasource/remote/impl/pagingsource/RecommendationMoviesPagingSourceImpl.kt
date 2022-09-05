package com.krykun.data.datasource.remote.impl.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.krykun.data.api.ApiService
import com.krykun.data.model.remote.movielistitem.MovieItem
import com.krykun.data.model.remote.movierecommendations.MovieRecommendationResponse

class RecommendationMoviesPagingSourceImpl(
    private val apiService: ApiService,
    private val movieId: Int
) : PagingSource<Int, MovieRecommendationResponse>() {

    /**
     * We're trying to get the next page number from the params, if it's null, we set it to 1. Then we
     * make a network call to the API, and return a LoadResult.Page with the data, the previous key,
     * and the next key
     *
     * @param params LoadParams<Int> - This is the page number that we want to load.
     * @return LoadResult.Page(
     *                 data = response.results as List<MovieItem>,
     *                 prevKey = null,
     *                 nextKey = when {
     *                     (response.page + 1) <= response.totalPages -> {
     *                         response.page + 1
     *                     }
     *                     else -> null
     *                 }
     *             )
     */
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieRecommendationResponse> {
        try {
            val nextPageNumber = params.key ?: 1
            val response = apiService.getRecommendationMovies(
                page = nextPageNumber,
                movieId = movieId
            )
            return LoadResult.Page(
                data = response.results as List<MovieRecommendationResponse>,
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
     * @param state PagingState<Int, MovieItem>
     * @return The key of the next page to load.
     */
    override fun getRefreshKey(state: PagingState<Int, MovieRecommendationResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}