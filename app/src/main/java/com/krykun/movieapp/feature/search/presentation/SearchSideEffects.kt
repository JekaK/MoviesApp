package com.krykun.movieapp.feature.search.presentation

sealed class SearchSideEffects {
    object TryReloadTrendingPage : SearchSideEffects()
    object UpdateSearchResult : SearchSideEffects()
}
