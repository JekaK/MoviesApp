package com.krykun.movieapp.feature.moviedetails

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import com.krykun.movieapp.R

@ExperimentalMotionApi
@Composable
fun MovieDetailsView() {
    var animateToEnd by remember {
        mutableStateOf(false)
    }
    val progress by animateFloatAsState(
        targetValue = if (animateToEnd) 1f else 0f,
        animationSpec = tween(450)
    )
    val context = LocalContext.current
    val motionScene = remember {
        context.resources.openRawResource(R.raw.details_motion_scene)
            .readBytes()
            .decodeToString()
    }
    MotionLayout(
        motionScene = MotionScene(content = motionScene),
        progress = progress,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
    ) {
        val contentAttr = motionProperties(id = "content")
        val titleAttr = motionProperties(id = "title")
        val imageAttr = motionProperties(id = "film_image")

        Box(
            modifier = Modifier
                .layoutId("content")
                .clip(
                    RoundedCornerShape(
                        topStart = contentAttr.value
                            .int("corner")
                            .toFloat()
                    )
                )
                .background(Color.LightGray)
                .clickable { animateToEnd = !animateToEnd }
        )
        Box(
            modifier = Modifier
                .layoutId("film_image")
                .clip(
                    RoundedCornerShape(
                        imageAttr.value
                            .int("corner")
                            .toFloat()
                    )
                )
                .background(Color.Green)
        )

        Text(
            text = "MotionLayout example",
            fontSize = titleAttr.value.fontSize("fontSize"),
            modifier = Modifier.layoutId("title")
        )

        Box(
            modifier = Modifier
                .layoutId("circle")
                .clip(CircleShape)
                .background(Color.Red)
        )
    }
//    }
}