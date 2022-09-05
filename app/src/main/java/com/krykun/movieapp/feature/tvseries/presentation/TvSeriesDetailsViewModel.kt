package com.krykun.movieapp.feature.tvseries.presentation

import android.annotation.SuppressLint
import androidx.compose.runtime.mutableStateOf
import com.krykun.domain.model.remote.tvdetails.TvDetails
import com.krykun.domain.usecase.remote.tvdetails.GetTvCastDetailsUseCase
import com.krykun.domain.usecase.remote.tvdetails.GetTvDetailsUseCase
import com.krykun.movieapp.base.BaseViewModel
import com.krykun.movieapp.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class TvSeriesDetailsViewModel @Inject constructor(
    appState: MutableStateFlow<AppState>,
    private val getTvDetailsUseCase: GetTvDetailsUseCase,
    private val getTvCastDetailsUseCase: GetTvCastDetailsUseCase,
) : BaseViewModel<TvSeriesDetailsSideEffects>(appState) {

    enum class MovieDetailsState {
        LOADING,
        DEFAULT,
        ERROR
    }

    val movieData = mutableStateOf<TvDetails?>(null)
    val movieDetailsState = mutableStateOf(MovieDetailsState.LOADING)
    val isRatingVisible = mutableStateOf(false)

    init {
        loadMovieDetails()
    }


    private fun loadMovieDetails() = intent {
        postSideEffect(TvSeriesDetailsSideEffects.ShowLoadingState)
        val castResult =
            getTvCastDetailsUseCase.getTvCastDetails(state.value.tvSeriesState.id)
        val result = getTvDetailsUseCase.getTvDetails(state.value.tvSeriesState.id)

        if (result.isSuccess && castResult.isSuccess) {
            val verifiedResponse = result.map {
                it.copy(
                    cast = castResult.getOrNull()
                )
            }
            reduce {
                state.value = state.value.copy(
                    tvSeriesState = TvSeriesDetailsState(
                        details = verifiedResponse.getOrNull()
                    )
                )
                state
            }
            postSideEffect(TvSeriesDetailsSideEffects.ShowMovieData(verifiedResponse.getOrNull()))
        } else {
            postSideEffect(TvSeriesDetailsSideEffects.ShowErrorState)
        }
    }

    fun updateMovieSelector() = intent {
        state.value.tvSeriesState.details?.let {
            reduce {
                state.value = state.value.copy(
                    playlistSelectState = state.value.playlistSelectState.copy(
                        tvDetails = it
                    )
                )
                state
            }
            postSideEffect(TvSeriesDetailsSideEffects.OpenPlaylistSelector)
        }
    }

}