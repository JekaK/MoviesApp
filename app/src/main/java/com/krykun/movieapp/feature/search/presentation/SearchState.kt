package com.krykun.movieapp.feature.search.presentation

data class SearchState(
    val query: String = "",
    val isLoading: Boolean = false,
)
