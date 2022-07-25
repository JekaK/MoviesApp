package com.krykun.movieapp.feature.person.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.krykun.movieapp.R
import com.krykun.movieapp.ext.noRippleClickable

@Composable
fun PersonTab(
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