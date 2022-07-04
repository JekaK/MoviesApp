package com.krykun.movieapp.custom

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.hypot

@Composable
fun CircularRevealLayout(
    modifier: Modifier = Modifier,
    coordinateX: Float = -1f,
    coordinateY: Float = -1f,
    color: Color = Color.Red,
    content: @Composable () -> Unit,
) {
    val radius = remember { mutableStateOf(0f) }
    val animatedRadius = remember { Animatable(0f) }
    val (width, height) = with(LocalConfiguration.current) {
        with(LocalDensity.current) { screenWidthDp.dp.toPx() to screenHeightDp.dp.toPx() }
    }
    val maxRadiusPx = hypot(width, height)
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .drawBehind {
                drawCircle(
                    color = color,
                    radius = radius.value,
                    center = if (coordinateX < 0f &&
                        coordinateY < 0f
                    ) {
                        Offset(size.width, 0f)
                    } else {
                        Offset(coordinateX, coordinateY)
                    },
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(animatedRadius.value),
            elevation = 0.dp,
            modifier = Modifier.fillMaxSize()
        ) {
            content()
        }
    }

    LaunchedEffect(false) {
        animatedRadius.animateTo(maxRadiusPx, animationSpec = tween()) {
            radius.value = value
        }
        animatedRadius.snapTo(0f)
    }
}