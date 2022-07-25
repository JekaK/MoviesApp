package com.krykun.movieapp.feature.search.presentation

sealed class SearchSideEffects {
    object TryReloadTrendingPage : SearchSideEffects()
    object UpdateSearchResult : SearchSideEffects()
    data class SetIsLoading(val isLoading: Boolean) : SearchSideEffects()

    object NavigateToMovie : SearchSideEffects()
    object NavigateToTvSeries : SearchSideEffects()
    object NavigateToPersonDetails : SearchSideEffects()

    data class SetSavedQuery(val query: String) : SearchSideEffects()
}
