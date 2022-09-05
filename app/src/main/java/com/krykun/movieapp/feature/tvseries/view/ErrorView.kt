package com.krykun.movieapp.feature.tvseries.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.krykun.movieapp.R

@Composable
fun ErrorView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.container_background)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.error_str),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}