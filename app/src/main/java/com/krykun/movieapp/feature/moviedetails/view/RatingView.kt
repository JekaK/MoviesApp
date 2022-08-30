package com.krykun.movieapp.feature.moviedetails.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Reviews
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.krykun.data.util.Constants
import com.krykun.domain.model.remote.moviedetails.MovieDetails
import com.krykun.movieapp.R
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun RatingView(
    isRatingVisible: MutableState<Boolean>,
    screenWidth: Dp,
    movieData: MutableState<MovieDetails?>
) {
    AnimatedVisibility(
        visible = isRatingVisible.value,
        enter = slideInHorizontally(
            initialOffsetX = { screenWidth.value.toInt() },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = 350f
            )
        ),
        exit = slideOutHorizontally()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(color = Color.Transparent)
                .offset(
                    y = (-60).dp,
                    x = 40.dp
                )
        ) {
            Card(
                modifier = Modifier
                    .background(Color.Transparent),
                shape = RoundedCornerShape(
                    topStart = 50.dp,
                    bottomStart = 50.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            modifier = Modifier.size(36.dp),
                            imageVector = Icons.Default.Star,
                            contentDescription = "",
                            tint = colorResource(id = R.color.star_yellow)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "${movieData.value?.voteAverage}/10",
                            fontWeight = FontWeight.SemiBold,
                            color = colorResource(id = R.color.black)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(
                            modifier = Modifier
                                .height(36.dp)
                                .widthIn()
                        ) {
                            val companiesList =
                                if ((movieData.value?.productionCompanies?.size ?: 0) > 3) {
                                    movieData.value?.productionCompanies?.subList(0, 3)
                                } else {
                                    movieData.value?.productionCompanies
                                }
                            companiesList?.forEachIndexed { index, productionCompany ->
                                CoilImage(
                                    imageModel = Constants.IMAGE_BASE_URL + productionCompany?.logoPath,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(36.dp)
                                        .offset(
                                            x = if (index == 0) {
                                                9.dp
                                            } else {
                                                (-9).dp * index
                                            }
                                        )
                                        .zIndex(index.toFloat())
                                        .border(
                                            width = 1.dp,
                                            color = Color.LightGray,
                                            shape = CircleShape
                                        )
                                        .clip(shape = CircleShape)
                                        .background(Color.White),
                                    contentScale = ContentScale.Inside,
                                    placeHolder = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
                                    error = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Companies",
                            fontWeight = FontWeight.SemiBold,
                            color = colorResource(id = R.color.black)
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                modifier = Modifier.size(36.dp),
                                imageVector = Icons.Default.Reviews,
                                contentDescription = "",
                                tint = colorResource(id = R.color.reviews_color)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "${movieData.value?.voteCount} votes",
                                fontWeight = FontWeight.SemiBold,
                                color = colorResource(id = R.color.black)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
            }
        }
    }
}