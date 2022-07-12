package com.krykun.movieapp.feature.search.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.krykun.data.util.Constants
import com.krykun.domain.model.search.SearchItem
import com.krykun.movieapp.R
import com.skydoves.landscapist.CircularReveal
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun MovieItemView(searchItem: SearchItem) {
    Column(
        modifier = Modifier.size(
            width = 128.dp,
            height = 250.dp
        )
    ) {
        CoilImage(
            imageModel = Constants.IMAGE_BASE_URL + searchItem.posterPath,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            circularReveal = CircularReveal(duration = 350),
            placeHolder = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
            error = ImageVector.vectorResource(id = R.drawable.ic_movie_placeholder),
        )
    }

}