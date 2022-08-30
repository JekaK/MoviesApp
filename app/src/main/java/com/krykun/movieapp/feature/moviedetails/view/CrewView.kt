package com.krykun.movieapp.feature.moviedetails.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.krykun.data.util.Constants
import com.krykun.domain.model.remote.moviecastdetails.Crew
import com.krykun.movieapp.R
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun CrewView(crewItem: Crew) {
    Column(
        modifier = Modifier
            .height(250.dp)
            .width(150.dp)
    ) {
        CoilImage(
            imageModel = Constants.IMAGE_BASE_URL + crewItem.profilePath,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            circularReveal = CircularReveal(duration = 500),
            placeHolder = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
            error = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder)
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = crewItem.name ?: "",
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = crewItem.department ?: "",
            fontWeight = FontWeight.Normal,
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 10.sp
        )
    }
}
