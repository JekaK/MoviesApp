package com.krykun.movieapp.feature.tvseries.presentation

import com.krykun.domain.model.remote.tvdetails.TvDetails
import com.krykun.movieapp.state.BaseDetails
import com.krykun.movieapp.state.DetailsState
import com.krykun.movieapp.state.LoadingState

data class TvSeriesDetailsState(
    override var id: Int = -1,
    override var details: TvDetails? = null,
    override var state: DetailsState = DetailsState.DEFAULT,
    override var isAdded: Boolean = true,
    override var loadingState: LoadingState = LoadingState.LOADING
) : BaseDetails<TvDetails>()
