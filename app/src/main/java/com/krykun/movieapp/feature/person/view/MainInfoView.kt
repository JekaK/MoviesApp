package com.krykun.movieapp.feature.person.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.EventListener
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.krykun.data.util.Constants
import com.krykun.domain.model.remote.persondetails.PersonDetails
import com.krykun.movieapp.R
import com.krykun.movieapp.custom.DynamicThemePrimaryColorsFromImage
import com.krykun.movieapp.custom.rememberDominantColorState
import com.krykun.movieapp.custom.verticalGradientScrim
import com.krykun.movieapp.ext.contrastAgainst
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.coil.CoilImage
import kotlinx.coroutines.launch

@Composable
fun MainPersonInfo(
    personDetails: MutableState<PersonDetails?>,
    modifier: Modifier = Modifier
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
            modifier = modifier
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