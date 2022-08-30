package com.krykun.movieapp.feature.moviedetails.view

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krykun.domain.model.remote.moviedetails.MovieDetails
import com.krykun.movieapp.R
import java.text.SimpleDateFormat
import java.util.*

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