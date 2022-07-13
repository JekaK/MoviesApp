package com.krykun.movieapp.feature.search.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krykun.data.util.Constants
import com.krykun.domain.model.search.MediaType
import com.krykun.domain.model.search.SearchItem
import com.krykun.movieapp.R
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun SearchItemView(searchItem: SearchItem) {
    when (searchItem.mediaType) {
        MediaType.MOVIE -> MovieView(searchItem = searchItem)
        MediaType.PERSON -> PersonView(searchItem = searchItem)
        MediaType.TV -> TvSeriesView(searchItem = searchItem)
    }
}

@Composable
fun MovieView(searchItem: SearchItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(colorResource(id = R.color.container_background))
            .clip(RoundedCornerShape(20.dp))
    ) {
        CoilImage(
            imageModel = Constants.IMAGE_BASE_URL + searchItem.posterPath,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            circularReveal = CircularReveal(duration = 350),
            placeHolder = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
            error = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.BottomCenter)
                .background(colorResource(id = R.color.bottom_bar_start))
        ) {
            Text(
                text = searchItem.title ?: "",
                color = Color.White,
                modifier = Modifier.padding(
                    start = 12.dp,
                    end = 12.dp,
                    top = 12.dp,
                    bottom = 15.dp
                ),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            searchItem.genre?.joinToString(separator = " | ")?.let {
                if (it.isNotEmpty()) {
                    Text(
                        text = it,
                        color = Color.White,
                        modifier = Modifier.padding(
                            start = 12.dp,
                            end = 12.dp,
                            top = 6.dp,
                            bottom = 15.dp
                        ),
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun TvSeriesView(searchItem: SearchItem) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(colorResource(id = R.color.container_background))
            .clip(RoundedCornerShape(20.dp))
    ) {
        CoilImage(
            imageModel = Constants.IMAGE_BASE_URL + searchItem.posterPath,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            circularReveal = CircularReveal(duration = 350),
            placeHolder = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
            error = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.BottomCenter)
                .background(colorResource(id = R.color.bottom_bar_start))
        ) {
            Text(
                text = searchItem.title ?: "",
                color = Color.White,
                modifier = Modifier.padding(
                    start = 12.dp,
                    end = 12.dp,
                    top = 12.dp,
                    bottom = 15.dp
                ),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            searchItem.genre?.joinToString(separator = " | ")?.let {
                Text(
                    text = it,
                    color = Color.White,
                    modifier = Modifier.padding(
                        start = 12.dp,
                        end = 12.dp,
                        top = 6.dp,
                        bottom = 15.dp
                    ),
                    fontWeight = FontWeight.Normal,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }
    }
}

@Composable
fun PersonView(searchItem: SearchItem) {
    Column(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CoilImage(
            imageModel = Constants.IMAGE_BASE_URL + searchItem.profilePath,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            circularReveal = CircularReveal(duration = 500),
            placeHolder = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
            error = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder)
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = searchItem.name ?: "",
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            fontSize = 14.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = searchItem.knownForDepartment ?: "",
            fontWeight = FontWeight.Normal,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 10.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}