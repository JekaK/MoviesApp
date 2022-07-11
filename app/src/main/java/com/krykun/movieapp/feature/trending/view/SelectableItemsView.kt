package com.krykun.movieapp.feature.trending.view

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
import com.krykun.movieapp.feature.trending.presentation.TrendingViewModel

@Composable
fun SelectableItemsView(
    items: List<SelectedMovieType>,
    selectedIndex: Int,
    viewModel: TrendingViewModel
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 16.dp, end = 16.dp)
    ) {
        items.forEachIndexed { index, s ->
            Text(
                text = s.title,
                fontSize = if (index == selectedIndex) {
                    24.sp
                } else {
                    16.sp
                },
                fontWeight = if (index == selectedIndex) {
                    FontWeight.SemiBold
                } else {
                    FontWeight.Normal
                },
                color = if (index == selectedIndex) {
                    Color.White
                } else {
                    Color.LightGray
                },
                modifier = Modifier.noRippleClickable {
                    viewModel.setSelectedMovieType(s)
                }
            )
            if (index != items.size - 1) {
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "|",
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
        }
    }
}