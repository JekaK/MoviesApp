package com.krykun.movieapp.feature.moviedetails.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.krykun.data.util.Constants
import com.krykun.domain.model.remote.MovieDiscoverItem
import com.krykun.domain.model.remote.tvdetails.Season
import com.krykun.movieapp.R
import com.krykun.movieapp.ext.noRippleClickable
import com.krykun.movieapp.feature.moviedetails.presentation.MovieDetailsViewModel
import com.krykun.movieapp.navigation.Screen
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun RecommendedMoviesView(
    recommendedMovies: LazyPagingItems<MovieDiscoverItem>,
    lazyListState: LazyListState,
    viewModel: MovieDetailsViewModel,
    navHostController: NavHostController
) {
    if (recommendedMovies.itemCount != 0 &&
        recommendedMovies.loadState.refresh is LoadState.NotLoading
    ) {
        Text(
            text = stringResource(R.string.recommendations),
            fontWeight = FontWeight.Bold,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(
                start = 24.dp,
                end = 24.dp
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize(),
        ) {
            itemsIndexed(items = recommendedMovies) { index, item ->
                Box(
                    modifier = Modifier
                        .padding(
                            start = if (index == 0) {
                                24.dp
                            } else {
                                2.dp
                            },
                            end = 2.dp
                        )
                        .noRippleClickable {
                            item?.id?.let {
                                viewModel.navigateToMovie(it)
                            }
                        }
                ) {
                    item?.let { MovieItemView(movieItem = it) }
                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
    } else {
        if (recommendedMovies.loadState.refresh is LoadState.Loading) {
            LoadingRecommendationsView()
        }
    }

}

@Composable
private fun MovieItemView(movieItem: MovieDiscoverItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CoilImage(
            imageModel = Constants.IMAGE_BASE_URL + movieItem.posterPath,
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(20.dp)),
            contentScale = ContentScale.Crop,
            circularReveal = CircularReveal(duration = 500),
            placeHolder = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
            error = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = movieItem.originalTitle ?: "",
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            fontSize = 14.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))
    }
}