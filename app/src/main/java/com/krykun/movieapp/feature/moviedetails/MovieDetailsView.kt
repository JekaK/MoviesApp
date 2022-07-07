package com.krykun.movieapp.feature.moviedetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Reviews
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.krykun.data.util.Constants
import com.krykun.domain.model.castdetails.Cast
import com.krykun.domain.model.castdetails.Crew
import com.krykun.domain.model.moviedetails.MovieDetails
import com.krykun.movieapp.R
import com.krykun.movieapp.feature.moviedetails.presentation.MovieDetailsSideEffects
import com.krykun.movieapp.feature.moviedetails.presentation.MovieDetailsViewModel
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectSideEffect
import java.text.SimpleDateFormat
import java.util.*

@ExperimentalMotionApi
@Composable
fun MovieDetailsView(
    viewModel: MovieDetailsViewModel = hiltViewModel(),
    navHostController: NavHostController
) {

    val movieData = remember {
        mutableStateOf<MovieDetails?>(null)
    }
    val movieDetailsState = remember {
        mutableStateOf(MovieDetailsState.LOADING)
    }
    val scope = rememberCoroutineScope()

    val isRatingVisible = remember {
        mutableStateOf(false)
    }
    Crossfade(targetState = movieDetailsState.value) {
        when (it) {
            MovieDetailsState.LOADING -> {
                LoadingView()
            }
            MovieDetailsState.DEFAULT -> {
                MovieDetailsView(
                    movieData = movieData,
                    navHostController = navHostController,
                    isRatingVisible = isRatingVisible
                )
            }
            MovieDetailsState.ERROR -> {

            }
        }
    }

    viewModel.collectSideEffect {
        handleSideEffects(
            it,
            movieData,
            isRatingVisible,
            movieDetailsState,
            scope
        )
    }
}

enum class MovieDetailsState {
    LOADING,
    DEFAULT,
    ERROR
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MovieDetailsView(
    movieData: MutableState<MovieDetails?>,
    navHostController: NavHostController,
    isRatingVisible: MutableState<Boolean>
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val scrollSate = rememberScrollState()
    CompositionLocalProvider(
        LocalOverScrollConfiguration provides null
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            BackBtn(navHostController = navHostController)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollSate)
            ) {
                HeaderView(
                    backdropPath = movieData.value?.backdropPath ?: ""
                )
                RatingView(
                    isRatingVisible = isRatingVisible,
                    screenWidth = screenWidth,
                    movieData = movieData
                )
                Column(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(start = 24.dp, end = 24.dp)) {
                        TitleView(movieData)
                    }
                    LazyRow {
                        items(count = movieData.value?.genres?.size ?: 0) { index ->
                            Text(
                                text = movieData.value?.genres?.get(index)?.name ?: "",
                                modifier = Modifier
                                    .padding(
                                        start = if (index == 0) {
                                            24.dp
                                        } else {
                                            2.dp
                                        },
                                        end = 2.dp,
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = Color.LightGray,
                                        shape = CircleShape
                                    )
                                    .padding(
                                        start = 16.dp,
                                        end = 16.dp,
                                        top = 5.dp,
                                        bottom = 5.dp
                                    ),
                                color = colorResource(id = R.color.white),

                                )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                    Column(modifier = Modifier.padding(start = 24.dp, end = 24.dp)) {

                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            text = stringResource(R.string.plot_summary),
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = movieData.value?.overview ?: "",
                            fontWeight = FontWeight.Normal,
                            color = Color.White.copy(alpha = 0.7f),
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Text(
                            text = stringResource(R.string.cast_and_crew),
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                LazyRow {
                    itemsIndexed(
                        movieData.value?.cast?.castAndCrew ?: listOf()
                    ) { index, item ->
                        if (item is Cast) {
                            CastView(castItem = item)
                        } else {
                            CrewView(crewItem = item as Crew)
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun TitleView(movieData: MutableState<MovieDetails?>) {
    Text(
        text = movieData.value?.originalTitle ?: "",
        fontWeight = FontWeight.Bold,
        color = Color.White,
        fontSize = 24.sp
    )
    Spacer(modifier = Modifier.height(16.dp))
    Row {
        Text(
            text = if (movieData.value?.releaseDate?.isNotEmpty() == true) {
                val calendar = Calendar.getInstance(TimeZone.getDefault())
                calendar.time = SimpleDateFormat("yyyy-MM-dd").parse(
                    movieData.value?.releaseDate ?: ""
                )
                calendar.get(Calendar.YEAR).toString()
            } else {
                ""
            },
            color = colorResource(id = R.color.light_gray_color),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "${movieData.value?.runtime?.toString() ?: ""} min",
            color = Color.LightGray,
        )
    }
    Spacer(modifier = Modifier.height(24.dp))
}

@Composable
fun CastView(castItem: Cast) {
    Column(
        modifier = Modifier
            .height(250.dp)
            .width(150.dp)
            .padding(start = 6.dp, end = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CoilImage(
            imageModel = Constants.IMAGE_BASE_URL + castItem.profilePath,
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
            text = castItem.name ?: "",
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            fontSize = 14.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = castItem.character ?: "",
            fontWeight = FontWeight.Normal,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 10.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun CrewView(crewItem: Crew) {
    Column(
        modifier = Modifier
            .height(250.dp)
            .width(150.dp)
    ) {
        CoilImage(
            imageModel = Constants.IMAGE_BASE_URL + crewItem.profilePath,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            circularReveal = CircularReveal(duration = 500),
            placeHolder = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
            error = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder)
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = crewItem.name ?: "",
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = crewItem.department ?: "",
            fontWeight = FontWeight.Normal,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 10.sp
        )
    }
}

@Composable
fun HeaderView(backdropPath: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(RoundedCornerShape(bottomStart = 40.dp))
    ) {
        CoilImage(
            imageModel = Constants.IMAGE_BASE_URL + backdropPath,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            circularReveal = CircularReveal(duration = 500),
            placeHolder = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
            error = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder)
        )
    }
}

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

@Composable
private fun BackBtn(navHostController: NavHostController) {
    Row(modifier = Modifier.zIndex(1f)) {
        Spacer(modifier = Modifier.width(24.dp))
        Column {
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .clickable {
                        navHostController.popBackStack()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = ""
                )
            }
        }
    }
}

fun handleSideEffects(
    sideEffects: MovieDetailsSideEffects,
    movieData: MutableState<MovieDetails?>,
    isRatingVisible: MutableState<Boolean>,
    movieDetailsState: MutableState<MovieDetailsState>,
    scope: CoroutineScope,
) {
    when (sideEffects) {
        is MovieDetailsSideEffects.ShowLoadingState -> {
            movieDetailsState.value =
                MovieDetailsState.LOADING
        }
        is MovieDetailsSideEffects.ShowErrorState -> {
            movieDetailsState.value =
                MovieDetailsState.ERROR
        }
        is MovieDetailsSideEffects.ShowMovieData -> {
            movieData.value = sideEffects.movieDetails
            movieDetailsState.value =
                MovieDetailsState.DEFAULT
            scope.launch {
                delay(300)
                isRatingVisible.value = true
            }
        }
    }
}
