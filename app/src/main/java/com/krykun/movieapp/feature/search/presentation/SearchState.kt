package com.krykun.movieapp.feature.search.presentation

data class SearchState(
    val query: String = "",
    val isLoading: Boolean = false,
    val currentPageIndex: Int = -1,
    val scrollOffset: Int = 0,
    val lastSavedPage: Int = 0,
)
