package com.krykun.data.api

import com.krykun.data.model.BasicMoviesResponse
import com.krykun.data.model.Genre
import com.krykun.data.model.GenresResponse
import com.krykun.data.model.MovieItem
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("discover/movie")
    suspend fun getDiscoverMovies(
        @Query("page") page: Int = 0,
        @Query("sort_by") sortBy: String? = "popularity.desc",
        @Query("include_adult") includeAdult: Boolean = true,
        @Query("include_video") includeVideo: Boolean = true,
    ): BasicMoviesResponse<MovieItem>

    @GET("genre/movie/list")
    suspend fun getGenres(): GenresResponse
}
