package com.krykun.movieapp.feature.search.presentation

import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.krykun.domain.model.remote.search.SearchItem
import com.krykun.domain.usecase.remote.search.MakeSearchUseCase
import com.krykun.movieapp.base.BaseViewModel
import com.krykun.movieapp.feature.moviedetails.presentation.MovieDetailsState
import com.krykun.movieapp.feature.person.presentation.PersonDetailsState
import com.krykun.movieapp.feature.tvseries.presentation.TvSeriesDetailsState
import com.krykun.movieapp.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    appState: MutableStateFlow<AppState>,
    private val makeSearchUseCase: MakeSearchUseCase
) : BaseViewModel<SearchSideEffects>(appState) {

    var searchResults: Flow<PagingData<SearchItem>>? = null

    private val _text = MutableStateFlow("")
    val text: StateFlow<String> = _text

    init {
        viewModelScope.launch {
            @OptIn(FlowPreview::class)
            _text.debounce(1000)
                .collect {
                    makeSearch(it)
                }
        }
    }

    fun updateText(text: String) = intent {
        _text.value = text
    }

    private fun makeSearch(query: String) = intent {
        reduce {
            state.value =
                state.value.copy(searchState = state.value.searchState.copy(query = query))
            state
        }

        searchResults = makeSearchUseCase.makeSearch(
            query,
            state.value.baseMoviesState.genres
        ).cachedIn(viewModelScope)
        postSideEffect(SearchSideEffects.UpdateSearchResult)
    }

    fun setSavedQuery() = intent {
        postSideEffect(SearchSideEffects.SetSavedQuery(state.value.searchState.query))
    }

    fun setIsLoading(isLoading: Boolean) = intent {
        reduce {
            state.value = state.value.copy(
                searchState = state.value.searchState.copy(
                    isLoading = isLoading
                )
            )
            state
        }
        postSideEffect(SearchSideEffects.SetIsLoading(isLoading = isLoading))
    }

    fun navigateToMovie(id: Int) = intent {
        reduce {
            state.value = state.value.copy(
                movieDetailsState = state.value.movieDetailsState +MovieDetailsState(id = id)
            )
            state
        }
        postSideEffect(SearchSideEffects.NavigateToMovie)
    }

    fun navigateToTvState(id: Int) = intent {
        reduce {
            state.value = state.value.copy(
                tvSeriesState = TvSeriesDetailsState(id = id)
            )
            state
        }
        postSideEffect(SearchSideEffects.NavigateToTvSeries)
    }

    fun navigateToPerson(id: Int) = intent {
        reduce {
            state.value = state.value.copy(
                personState = PersonDetailsState(id = id)
            )
            state
        }
        postSideEffect(SearchSideEffects.NavigateToPersonDetails)
    }

    fun handleLoadSearchItemsState(loadStates: LoadStates) = intent {
        val errorLoadState = arrayOf(
            loadStates.append,
            loadStates.prepend,
            loadStates.refresh
        ).filterIsInstance(LoadState.Error::class.java).firstOrNull()
        val throwable = errorLoadState?.error
        if (throwable != null &&
            (throwable as HttpException).code() != 422
        ) {
            postSideEffect(SearchSideEffects.TryReloadPage)
        }
        if (throwable != null &&
            (throwable as HttpException).code() == 422
        ) {
            setIsLoading(false)
        }
    }

    fun setScrollOffset(scrollOffset: Int) = intent {
        reduce {
            state.value = state.value.copy(
                searchState = state.value.searchState.copy(
                    scrollOffset = scrollOffset
                )
            )
            state
        }
    }

    fun getCurrentPageAndScrollOffset() = intent {
        val page = when {
            state.value.searchState.lastSavedPage > 0 -> state.value.searchState.lastSavedPage
            state.value.searchState.scrollOffset > 0f -> state.value.searchState.scrollOffset
            else -> 0
        }
        postSideEffect(
            SearchSideEffects.GetCurrentDiscoverPageAndScrollOffset(page)
        )
    }

    fun setLastScrolledPage(index: Int) = intent {
        if (index != state.value.searchState.lastSavedPage) {
            reduce {
                state.value = state.value.copy(
                    searchState = state.value.searchState.copy(
                        lastSavedPage = index
                    )
                )
                state
            }
        }
    }
}