package com.krykun.movieapp.state

import com.krykun.domain.model.Genre

data class BaseMoviesState(
    val genres: List<Genre> = listOf(),
)
