package com.krykun.movieapp.feature.person.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import coil.EventListener
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.flowlayout.SizeMode
import com.krykun.data.util.Constants
import com.krykun.domain.model.remote.personcombinedcredits.Cast
import com.krykun.domain.model.remote.personcombinedcredits.Crew
import com.krykun.domain.model.remote.persondetails.PersonDetails
import com.krykun.movieapp.R
import com.krykun.movieapp.custom.DynamicThemePrimaryColorsFromImage
import com.krykun.movieapp.custom.rememberDominantColorState
import com.krykun.movieapp.custom.verticalGradientScrim
import com.krykun.movieapp.ext.contrastAgainst
import com.krykun.movieapp.ext.noRippleClickable
import com.krykun.movieapp.feature.person.presentation.PersonSideEffects
import com.krykun.movieapp.feature.person.presentation.PersonViewModel
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PersonView(
    viewModel: PersonViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val personDetails = remember { mutableStateOf<PersonDetails?>(null) }
    val personDetailsState = remember { mutableStateOf(PersonDetailsState.LOADING) }
    val selectedPersonTab = remember {
        mutableStateOf(PersonTabs.FILMOGRAPHY)
    }


    viewModel.collectSideEffect {
        handleSideEffects(
            sideEffects = it,
            personDetails = personDetails,
            personDetailsState = personDetailsState
        )
    }
    LaunchedEffect(key1 = lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.subscribeToState()
                .collect {
                    selectedPersonTab.value = when (it.selectedPersonTab) {
                        com.krykun.movieapp.feature.person.presentation.PersonTabs.FILMOGRAPHY -> PersonTabs.FILMOGRAPHY
                        com.krykun.movieapp.feature.person.presentation.PersonTabs.PRODUCTION -> PersonTabs.PRODUCTION
                    }
                }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BackBtn(navHostController)
        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                item {
                    MainPersonInfo(personDetails = personDetails)
                }
                item {
                    PersonMovies(
                        selectedPersonTab = selectedPersonTab,
                        personDetails = personDetails,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

enum class PersonTabs {
    FILMOGRAPHY,
    PRODUCTION
}

@Composable
private fun PersonMovies(
    selectedPersonTab: MutableState<PersonTabs>,
    personDetails: MutableState<PersonDetails?>,
    viewModel: PersonViewModel
) {
    Column {
        Card(
            elevation = 16.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(
                    start = 36.dp,
                    end = 36.dp,
                ),
            shape = CircleShape,
            backgroundColor = Color.White,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                PersonTab(
                    modifier = Modifier
                        .weight(1f),
                    isSelected = selectedPersonTab.value == PersonTabs.FILMOGRAPHY,
                    text = stringResource(id = R.string.filmography)
                ) {
                    viewModel.setPersonTabSelected(PersonTabs.FILMOGRAPHY)
                }
                Spacer(modifier = Modifier.width(16.dp))

                PersonTab(
                    modifier = Modifier
                        .weight(1f),
                    isSelected = selectedPersonTab.value == PersonTabs.PRODUCTION,
                    text = stringResource(id = R.string.production)
                ) {
                    viewModel.setPersonTabSelected(PersonTabs.PRODUCTION)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedVisibility(visible = selectedPersonTab.value == PersonTabs.FILMOGRAPHY) {
            CastViewList(
                castItems = personDetails.value?.personCombinedCredits?.cast ?: listOf()
            )
        }
        AnimatedVisibility(visible = selectedPersonTab.value == PersonTabs.PRODUCTION) {
            CrewViewList(personDetails.value?.personCombinedCredits?.crew ?: listOf())
        }
    }
}

@Composable
private fun PersonTab(
    modifier: Modifier,
    isSelected: Boolean,
    text: String,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .height(50.dp)
            .padding(
                start = 8.dp,
                end = 8.dp,
                top = 8.dp,
                bottom = 8.dp
            )
            .clip(CircleShape)
            .background(
                if (isSelected) {
                    colorResource(id = R.color.selected_container)
                } else {
                    Color.Transparent
                }
            )
            .noRippleClickable { onClick() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            color = if (isSelected) {
                Color.White
            } else {
                Color.LightGray
            },
        )
    }
}

@Composable
private fun MainPersonInfo(
    personDetails: MutableState<PersonDetails?>
) {

    val surfaceColor = MaterialTheme.colors.surface
    val dominantColorState = rememberDominantColorState { color ->
        color.contrastAgainst(surfaceColor) >= 3f
    }
    val parentOffsetState = remember {
        mutableStateOf(Offset(0f, 0f))
    }
    val context = LocalContext.current
    val url = Constants.IMAGE_BASE_URL + personDetails.value?.profilePath
    val scope = rememberCoroutineScope()

    DynamicThemePrimaryColorsFromImage(dominantColorState) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(330.dp)
                .verticalGradientScrim(
                    color = MaterialTheme.colors.primary,
                    startYPercentage = 1f,
                    endYPercentage = 0.5f
                )
                .onGloballyPositioned {
                    val offset = it.positionInRoot()
                    parentOffsetState.value = offset
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            CoilImage(
                imageModel = url,
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                circularReveal = CircularReveal(duration = 350),
                placeHolder = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
                error = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
                imageLoader = {
                    ImageLoader.Builder(context)
                        .eventListener(object : EventListener {
                            override fun onSuccess(
                                request: ImageRequest,
                                result: SuccessResult
                            ) {
                                super.onSuccess(request, result)
                                scope.launch {
                                    dominantColorState.updateColorsFromImageUrl(url)
                                }
                            }
                        })
                        .build()
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = personDetails.value?.name ?: "",
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (personDetails.value?.placeOfBirth?.isNotEmpty() == true) {
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Icon(
                        imageVector = Icons.Default.PinDrop,
                        contentDescription = "",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = personDetails.value?.placeOfBirth ?: "",
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun CastViewList(castItems: List<Cast>) {
    FlowRow(
        mainAxisSize = SizeMode.Expand,
        mainAxisAlignment = FlowMainAxisAlignment.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (castItems.isNotEmpty()) {
            castItems.forEach {
                CastView(castItem = it)
            }
        } else {
            EmptyView()
        }
    }
}

@Composable
private fun CrewViewList(crewItems: List<Crew>) {
    FlowRow(
        mainAxisSize = SizeMode.Expand,
        mainAxisAlignment = FlowMainAxisAlignment.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (crewItems.isNotEmpty()) {
            crewItems.forEach {
                CrewView(crewItem = it)
            }
        } else {
            EmptyView()
        }
    }
}

@Composable
private fun CastView(castItem: Cast) {
    Box(
        modifier = Modifier
            .width(128.dp)
            .height(200.dp)
            .padding(
                start = 8.dp,
                end = 8.dp,
                top = 8.dp,
                bottom = 8.dp
            )
            .background(colorResource(id = R.color.container_background))
            .clip(RoundedCornerShape(20.dp))
    ) {
        CoilImage(
            imageModel = Constants.IMAGE_BASE_URL + castItem.posterPath,
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
                text = castItem.title ?: "",
                color = Color.White,
                modifier = Modifier.padding(
                    start = 12.dp,
                    end = 12.dp,
                    top = 6.dp,
                    bottom = 15.dp
                ),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = castItem.character.toString(),
                color = Color.White,
                modifier = Modifier.padding(
                    start = 12.dp,
                    end = 12.dp,
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

@Composable
private fun CrewView(crewItem: Crew) {
    Box(
        modifier = Modifier
            .width(128.dp)
            .height(200.dp)
            .padding(
                start = 8.dp,
                end = 8.dp,
                top = 8.dp,
                bottom = 8.dp
            )
            .background(colorResource(id = R.color.container_background))
            .clip(RoundedCornerShape(20.dp))
    ) {
        CoilImage(
            imageModel = Constants.IMAGE_BASE_URL + crewItem.posterPath,
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
                text = crewItem.title ?: "",
                color = Color.White,
                modifier = Modifier.padding(
                    start = 12.dp,
                    end = 12.dp,
                    top = 6.dp,
                    bottom = 15.dp
                ),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = crewItem.job.toString(),
                color = Color.White,
                modifier = Modifier.padding(
                    start = 12.dp,
                    end = 12.dp,
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
    sideEffects: PersonSideEffects,
    personDetails: MutableState<PersonDetails?>,
    personDetailsState: MutableState<PersonDetailsState>
) {
    when (sideEffects) {
        is PersonSideEffects.ShowPersonDetailsData -> {
            personDetailsState.value = PersonDetailsState.DEFAULT
            personDetails.value = sideEffects.personDetails
        }
        is PersonSideEffects.ShowErrorState -> {
            personDetailsState.value = PersonDetailsState.ERROR
        }
        is PersonSideEffects.ShowLoadingState -> {
            personDetailsState.value = PersonDetailsState.LOADING
        }
    }
}

enum class PersonDetailsState {
    LOADING,
    DEFAULT,
    ERROR
}

