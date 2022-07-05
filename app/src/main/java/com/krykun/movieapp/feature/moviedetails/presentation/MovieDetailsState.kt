package com.krykun.movieapp.feature.moviedetails.presentation

import com.krykun.domain.model.moviedetails.MovieDetails

data class MovieDetailsState(
    val movieId: Int = -1,
    val movieData: MovieDetails? = null,
    val movieState: MovieState = MovieState.DEFAULT
)

enum class MovieState {
    DEFAULT,
    LOADING,
    ERROR
}