package com.krykun.movieapp.feature.tvseries.presentation

import com.krykun.domain.model.tvdetails.TvDetails

sealed class TvSeriesDetailsSideEffects {
    data class ShowMovieData(val tvDetails: TvDetails?) : TvSeriesDetailsSideEffects()
    object ShowLoadingState : TvSeriesDetailsSideEffects()
    object ShowErrorState : TvSeriesDetailsSideEffects()
}