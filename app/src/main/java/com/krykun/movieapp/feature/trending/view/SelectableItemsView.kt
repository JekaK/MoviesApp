package com.krykun.movieapp.feature.trending.view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krykun.movieapp.ext.noRippleClickable
import com.krykun.movieapp.feature.trending.presentation.SelectedMovieType

@Composable
fun SelectableItemsView(
    items: List<SelectedMovieType>,
    selectedIndex: Int,
    onClick: (SelectedMovieType) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
    ) {
        TextItem(items[0], selectedIndex, 0, onClick)
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "|",
            color = Color.White
        )
        Spacer(modifier = Modifier.width(16.dp))
        TextItem(items[1], selectedIndex, 1, onClick)
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "|",
            color = Color.White
        )
        Spacer(modifier = Modifier.width(16.dp))
        TextItem(items[2], selectedIndex, 2, onClick)
    }
}

@Composable
private fun TextItem(
    item: SelectedMovieType,
    selectedIndex: Int,
    currentIndex: Int,
    onClick: (SelectedMovieType) -> Unit
) {
    Text(
        text = item.title,
        fontSize = if (currentIndex == selectedIndex) {
            24.sp
        } else {
            16.sp
        },
        fontWeight = if (currentIndex == selectedIndex) {
            FontWeight.SemiBold
        } else {
            FontWeight.Normal
        },
        color = if (currentIndex == selectedIndex) {
            Color.White
        } else {
            Color.LightGray
        },
        modifier = Modifier
            .noRippleClickable {
                onClick(item)
            }
            .animateContentSize()
    )
}