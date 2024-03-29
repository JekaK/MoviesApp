package com.krykun.movieapp.feature.moviedetails.presentation

import com.krykun.domain.model.remote.moviedetails.MovieDetails
import com.krykun.movieapp.state.BaseDetails
import com.krykun.movieapp.state.DetailsState
import com.krykun.movieapp.state.LoadingState

data class MovieDetailsState(
    override var id: Int = -1,
    override var details: MovieDetails? = null,
    override var state: DetailsState = DetailsState.DEFAULT,
    override var isAdded: Boolean = true,
    override var loadingState: LoadingState = LoadingState.LOADING
):BaseDetails<MovieDetails>()