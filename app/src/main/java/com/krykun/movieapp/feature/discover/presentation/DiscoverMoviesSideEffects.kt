package com.krykun.movieapp.feature.discover.presentation

sealed class DiscoverMoviesSideEffects {
    data class TriggerOnPageChanged(val index: Int) : DiscoverMoviesSideEffects()
    data class GetCurrentDiscoverPageAndScrollOffset(
        val currentPageAndOffset: Int
    ) : DiscoverMoviesSideEffects()

    object TryReloadDiscoverPage : DiscoverMoviesSideEffects()
    object NavigateToMovie : DiscoverMoviesSideEffects()
}

sealed class DiscoverMoviesSideEffectsMiddleware {
    data class TriggerOnPageChanged(val index: Int) : DiscoverMoviesSideEffectsMiddleware()
    object GetCurrentDiscoverPageAndScrollOffset : DiscoverMoviesSideEffectsMiddleware()
    data class SetLastScrolledPage(val page: Int) : DiscoverMoviesSideEffectsMiddleware()
    data class NavigateToMovie(val movieId: Int) : DiscoverMoviesSideEffectsMiddleware()
}