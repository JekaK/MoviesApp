package com.krykun.movieapp.feature.tvseries.presentation

import androidx.lifecycle.ViewModel
import com.krykun.domain.usecase.GetTvCastDetailsUseCase
import com.krykun.domain.usecase.GetTvDetailsUseCase
import com.krykun.movieapp.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class TvSeriesDetailsViewModel @Inject constructor(
    appState: MutableStateFlow<AppState>,
    private val getTvDetailsUseCase: GetTvDetailsUseCase,
    private val getTvCastDetailsUseCase: GetTvCastDetailsUseCase
) : ViewModel(),
    ContainerHost<MutableStateFlow<AppState>, TvSeriesDetailsSideEffects> {
    override val container =
        container<MutableStateFlow<AppState>, TvSeriesDetailsSideEffects>(appState)

    init {
        loadMovieDetails()
    }

    private fun loadMovieDetails() = intent {
        postSideEffect(TvSeriesDetailsSideEffects.ShowLoadingState)
        val castResult =
            getTvCastDetailsUseCase.getTvCastDetails(state.value.tvSeriesState.tvId)
        val result = getTvDetailsUseCase.getTvDetails(state.value.tvSeriesState.tvId)

        if (result.isSuccess && castResult.isSuccess) {
            val verifiedResponse = result.map {
                it.copy(
                    cast = castResult.getOrNull()
                )
            }
            reduce {
                state.value = state.value.copy(
                    tvSeriesState = TvSeriesDetailsState(
                        tvDetails = verifiedResponse.getOrNull()
                    )
                )
                state
            }
            postSideEffect(TvSeriesDetailsSideEffects.ShowMovieData(verifiedResponse.getOrNull()))
        } else {
            postSideEffect(TvSeriesDetailsSideEffects.ShowErrorState)
        }

    }

}