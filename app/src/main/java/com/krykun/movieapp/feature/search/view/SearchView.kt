package com.krykun.movieapp.feature.search.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalTextToolbar
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.krykun.domain.model.search.SearchItem
import com.krykun.movieapp.R
import com.krykun.movieapp.custom.EmptyTextToolbar
import com.krykun.movieapp.ext.clearFocusOnKeyboardDismiss
import com.krykun.movieapp.ext.collectAndHandleState
import com.krykun.movieapp.feature.search.presentation.SearchSideEffects
import com.krykun.movieapp.feature.search.presentation.SearchViewModel
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SearchView(viewModel: SearchViewModel) {

    var popularMovies: LazyPagingItems<SearchItem>? =
        viewModel.searchResults?.collectAndHandleState(viewModel::handleLoadSearchItemsState)
    val markIsModified = remember {
        mutableStateOf(false)
    }
    if (markIsModified.value) {
        markIsModified.value = false
        popularMovies =
            viewModel.searchResults?.collectAndHandleState(viewModel::handleLoadSearchItemsState)
    }

    viewModel.collectSideEffect {
        handleSideEffects(
            it,
            markIsModified,
            popularMovies
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            SearchBar(viewModel = viewModel)
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Adaptive(minSize = 128.dp)
            ) {
                items(popularMovies?.itemCount ?: 0) { index ->
                    Box(Modifier.padding(8.dp)) {
                        popularMovies?.get(index)?.let { SearchItemView(it) }
                    }
                }
            }
        }
    }
}

private fun handleSideEffects(
    sideEffects: SearchSideEffects,
    markIsModified: MutableState<Boolean>,
    popularMovies: LazyPagingItems<SearchItem>?
) {
    when (sideEffects) {
        SearchSideEffects.TryReloadTrendingPage -> {
            popularMovies?.retry()
        }
        SearchSideEffects.UpdateSearchResult -> {
            markIsModified.value = true
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel,
) {
    Card(
        modifier = modifier.padding(
            start = 16.dp,
            end = 16.dp,
            top = 16.dp
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = 16.dp,
        contentColor = colorResource(id = R.color.light_gray_color)
    ) {
        Column {
            var query: String by rememberSaveable {
                mutableStateOf("")
            }

            var showClearIcon by rememberSaveable { mutableStateOf(false) }
            if (query.isEmpty()) {
                showClearIcon = false
            } else if (query.isNotEmpty()) {
                showClearIcon = true
            }

            CompositionLocalProvider(
                LocalTextToolbar provides EmptyTextToolbar,
                LocalOverScrollConfiguration provides null
            ) {
                TextField(
                    value = query,
                    onValueChange = { onQueryChanged ->
                        query = onQueryChanged
                        if (onQueryChanged.isNotEmpty()) {
                            viewModel.updateText(query)
                        }
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            tint = MaterialTheme.colors.onBackground,
                            contentDescription = "Search icon"
                        )
                    },
                    trailingIcon = {
                        AnimatedVisibility(
                            visible = showClearIcon,
                            enter = scaleIn(),
                            exit = scaleOut()
                        ) {
                            IconButton(onClick = {
                                query = ""
                            }) {
                                Icon(
                                    imageVector = Icons.Rounded.Clear,
                                    tint = MaterialTheme.colors.onBackground,
                                    contentDescription = "Clear icon"
                                )
                            }
                        }
                    },
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Black,
                        backgroundColor = Color.Transparent,
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    placeholder = {
                        Text(
                            text = stringResource(
                                R.string.hint_search_query,
                            ),
                            color = Color.Gray
                        )
                    },
                    textStyle = MaterialTheme.typography.subtitle1,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colors.background, shape = RectangleShape)
                        .clearFocusOnKeyboardDismiss()
                )
            }
        }
    }
}