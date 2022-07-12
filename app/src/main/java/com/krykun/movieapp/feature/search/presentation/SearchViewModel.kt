package com.krykun.movieapp.feature.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import com.krykun.domain.model.search.SearchItem
import com.krykun.domain.usecase.MakeSearchUseCase
import com.krykun.movieapp.feature.trending.presentation.TrendingMoviesSideEffects
import com.krykun.movieapp.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    appState: MutableStateFlow<AppState>,
    private val makeSearchUseCase: MakeSearchUseCase
) : ViewModel(), ContainerHost<MutableStateFlow<AppState>, SearchSideEffects> {

    override val container = container<MutableStateFlow<AppState>, SearchSideEffects>(appState)
    var searchResults: Flow<PagingData<SearchItem>>? = null

    private val _text = MutableStateFlow("")
    val text: StateFlow<String> = _text

    init {
        viewModelScope.launch {
            @OptIn(FlowPreview::class)
            _text.debounce(1000)
                .collect{
                    makeSearch(it)
                }
        }
    }

    fun updateText(text: String) {
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
        )
        postSideEffect(SearchSideEffects.UpdateSearchResult)
    }

    fun handleLoadSearchItemsState(loadStates: LoadStates) = intent {
        val errorLoadState = arrayOf(
            loadStates.append,
            loadStates.prepend,
            loadStates.refresh
        ).filterIsInstance(LoadState.Error::class.java).firstOrNull()
        val throwable = errorLoadState?.error
        if (throwable != null) {
            postSideEffect(SearchSideEffects.TryReloadTrendingPage)
        }
    }

}