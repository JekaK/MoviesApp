package com.krykun.movieapp.feature.discover.middleware

import androidx.paging.PagingData
import com.krykun.domain.model.remote.MovieDiscoverItem
import com.krykun.movieapp.feature.discover.presentation.DiscoverMoviesSideEffects
import com.krykun.movieapp.feature.discover.presentation.DiscoverMoviesSideEffectsMiddleware
import com.krykun.movieapp.state.AppState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.orbitmvi.orbit.Container

interface IDiscoverMoviesMiddleware {

    fun init(
        container: Container<MutableStateFlow<AppState>, DiscoverMoviesSideEffects>,
        viewModelScope: CoroutineScope,
        callback: (Flow<PagingData<MovieDiscoverItem>>) -> Unit
    )

    fun reduce(discoverMiddlewaresMoviesSideEffects: DiscoverMoviesSideEffectsMiddleware)
}