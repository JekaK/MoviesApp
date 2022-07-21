package com.krykun.movieapp.feature.playlist.main.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.krykun.data.util.Constants
import com.krykun.domain.model.local.Movie
import com.krykun.domain.model.local.Playlist
import com.krykun.movieapp.R
import com.krykun.movieapp.ext.scrollEnabled
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun PlaylistItemView(playlist: Playlist, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(
                start = 16.dp,
                bottom = 16.dp,
                top = 16.dp
            )
            .background(Color.Transparent)
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 75.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 20.dp,
                        bottomStart = 20.dp
                    )
                )
                .height(160.dp)
                .background(colorResource(id = R.color.bottom_bar_end))
                .padding(top = 80.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = playlist.name,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(start = 16.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Icon(
                imageVector = Icons.Default.ArrowForwardIos, contentDescription = "",
                tint = Color.White,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(24.dp)
            )
        }
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(start = 16.dp)
                .scrollEnabled(enabled = false)
        ) {
            itemsIndexed(items = playlist.movieList) { index, item ->
                MovieItemView(
                    movie = item,
                    modifier = if (index > 0) {
                        Modifier
                            .offset(
                                x = ((-50).dp) * index / 2,
                                y = if (index % 2 == 0) {
                                    5.dp
                                } else {
                                    (-5).dp
                                }
                            )
                            .zIndex((playlist.movieList.size - index).toFloat())
                    } else {
                        Modifier
                            .zIndex((playlist.movieList.size - index).toFloat())
                            .offset(
                                y = if (index % 2 == 0) {
                                    5.dp
                                } else {
                                    (-5).dp
                                }
                            )
                    }
                )
            }
        }
    }
}

@Composable
private fun MovieItemView(modifier: Modifier = Modifier, movie: Movie) {
    Box(
        modifier = modifier
            .width(100.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        CoilImage(
            imageModel = Constants.IMAGE_BASE_URL + movie.poster,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
            circularReveal = CircularReveal(duration = 350),
            placeHolder = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
            error = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder)
        )
    }
}