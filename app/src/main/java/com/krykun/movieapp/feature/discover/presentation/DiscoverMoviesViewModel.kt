package com.krykun.movieapp.feature.discover.presentation

import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.krykun.domain.model.remote.MovieDiscoverItem
import com.krykun.movieapp.base.BaseViewModel
import com.krykun.movieapp.feature.discover.middleware.DiscoverMoviesMiddleware
import com.krykun.movieapp.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import javax.inject.Inject

@HiltViewModel
class DiscoverMoviesViewModel @Inject constructor(
    appState: MutableStateFlow<AppState>,
    private val discoverMoviesMiddleware: DiscoverMoviesMiddleware
) : BaseViewModel<DiscoverMoviesSideEffects>(appState) {

    lateinit var getDiscoverMovies: Flow<PagingData<MovieDiscoverItem>>

    init {
        discoverMoviesMiddleware.init(container, viewModelScope) {
            getDiscoverMovies = it
        }
    }

    fun getCurrentPageAndScrollOffset() = intent {
        discoverMoviesMiddleware.getCurrentPageAndScrollOffset()
    }

    fun setLastScrolledPage(index: Int) = intent {
        discoverMoviesMiddleware.setLastScrolledPage(index)
    }

    fun triggerOnPageChanged(index: Int) = intent {
        discoverMoviesMiddleware.triggerOnPageChanged(index)
    }

    fun navigateToMovieDetails(movieId: Int) = intent {
        discoverMoviesMiddleware.navigateToMovieDetails(movieId)
    }

    /**
     * If any of the load states are in error, post a side effect to try reloading the page
     *
     * @param loadStates LoadStates - This is the LoadStates object that is passed to the
     * PagingDataAdapter.
     */
    fun handleLoadState(loadStates: LoadStates) = intent {
        val errorLoadState = arrayOf(
            loadStates.append,
            loadStates.prepend,
            loadStates.refresh
        ).filterIsInstance(LoadState.Error::class.java).firstOrNull()
        val loadState = arrayOf(
            loadStates.append,
            loadStates.prepend,
            loadStates.refresh
        ).filterIsInstance(LoadState.Loading::class.java).firstOrNull()

        val throwable = errorLoadState?.error
        if (throwable != null || loadState == null) {
            postSideEffect(DiscoverMoviesSideEffects.TryReloadDiscoverPage)
        }
    }
}