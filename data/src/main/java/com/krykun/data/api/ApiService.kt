package com.krykun.data.api

import com.krykun.data.model.BasicMoviesResponse
import com.krykun.data.model.castdetails.CastDetailsResponse
import com.krykun.data.model.genre.GenresResponse
import com.krykun.data.model.moviedetails.MovieDetailsResponse
import com.krykun.data.model.movielistitem.MovieItem
import com.krykun.data.model.movies.MovieItemResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
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
    suspend fun getGenres(): Response<GenresResponse>

    @GET("movie/{id}")
    suspend fun getMovieDetails(@Path("id") movieId: Int): Response<MovieDetailsResponse>

    @GET("movie/{id}/credits")
    suspend fun getCastDetails(@Path("id") movieId: Int): Response<CastDetailsResponse>

    @GET("trending/movie/week")
    suspend fun getTrendingMovies(
        @Query("page") page: Int = 0,
    ): BasicMoviesResponse<MovieItemResponse>

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 0,
    ): BasicMoviesResponse<MovieItemResponse>
}
