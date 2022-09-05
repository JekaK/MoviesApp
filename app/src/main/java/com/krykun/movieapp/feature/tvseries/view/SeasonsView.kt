package com.krykun.movieapp.feature.tvseries.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.krykun.domain.model.remote.tvdetails.Season

@Composable
fun SeasonsView(seasonsList: List<Season?>) {
    LazyRow {
        itemsIndexed(items = seasonsList) { index, item ->
            Box(
                modifier = Modifier.padding(
                    start = if (index == 0) {
                        24.dp
                    } else {
                        8.dp
                    }, end = if (index == seasonsList.size - 1) {
                        24.dp
                    } else {
                        8.dp
                    }
                )
            ) {
                item?.let { SeasonsItemView(seasonsItem = it) }
            }
        }
    }
}
