package com.krykun.movieapp.feature.tvseries.presentation

import com.krykun.domain.model.remote.tvdetails.TvDetails

data class TvSeriesDetailsState(
    val tvId: Int = -1,
    val tvDetails: TvDetails? = null,
    val tvState: TvSeriesState = TvSeriesState.DEFAULT
)

enum class TvSeriesState {
    DEFAULT,
    LOADING,
    ERROR
}