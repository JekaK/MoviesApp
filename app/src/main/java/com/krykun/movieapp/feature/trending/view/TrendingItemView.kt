package com.krykun.movieapp.feature.trending.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krykun.data.util.Constants
import com.krykun.domain.model.trending.TrendingMovie
import com.krykun.movieapp.R
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun TrendingItemView(
    modifier: Modifier,
    moviesItem: TrendingMovie,
) {
    Box(
        modifier = modifier
            .width(350.dp)
            .background(colorResource(id = R.color.container_background))
    ) {
        CoilImage(
            imageModel = Constants.IMAGE_BASE_URL + moviesItem.backdropPath,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
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
                text = moviesItem.title ?: "",
                color = Color.White,
                modifier = Modifier.padding(
                    start = 12.dp,
                    end = 12.dp,
                    top = 12.dp,
                ),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier.padding(
                    start = 12.dp,
                    end = 12.dp,
                    top = 6.dp,
                    bottom = 15.dp
                )
            ) {
                moviesItem.mappedGenreIds.forEachIndexed { index, s ->
                    Text(
                        text = s,
                        color = Color.White,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                colorResource(id = R.color.chip_color)
                            )
                            .padding(
                                start = 12.dp,
                                end = 12.dp,
                                top = 5.dp,
                                bottom = 5.dp
                            ),
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                }
            }
        }
    }
}