package com.krykun.movieapp.feature.moviedetails.presentation

import com.krykun.domain.model.remote.moviedetails.MovieDetails

data class MovieDetailsState(
    val movieId: Int = -1,
    val movieData: MovieDetails? = null,
    val movieState: MovieState = MovieState.DEFAULT,
    val isAdded: Boolean = true
)

enum class MovieState {
    DEFAULT,
    LOADING,
    ERROR
}