package com.krykun.movieapp.feature.tvseries.presentation

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.krykun.domain.usecase.local.AddMovieToPlaylistUseCase
import com.krykun.domain.usecase.local.CheckIsMovieAddedUseCase
import com.krykun.domain.usecase.remote.tvdetails.GetTvCastDetailsUseCase
import com.krykun.domain.usecase.remote.tvdetails.GetTvDetailsUseCase
import com.krykun.movieapp.R
import com.krykun.movieapp.base.BaseViewModel
import com.krykun.movieapp.feature.moviedetails.presentation.MovieDetailsSideEffects
import com.krykun.movieapp.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class TvSeriesDetailsViewModel @Inject constructor(
    private val context: Context,
    appState: MutableStateFlow<AppState>,
    private val getTvDetailsUseCase: GetTvDetailsUseCase,
    private val getTvCastDetailsUseCase: GetTvCastDetailsUseCase,
    private val addMovieToPlaylistUseCase: AddMovieToPlaylistUseCase,
    private val checkIsMovieAddedUseCase: CheckIsMovieAddedUseCase
) : BaseViewModel<TvSeriesDetailsSideEffects>(appState) {

    init {
        loadMovieDetails()
        checkIsAdded()
    }

    private fun checkIsAdded() = intent {
        viewModelScope.launch {
            checkIsMovieAddedUseCase.checkIsMovieInPlaylist(container.stateFlow.value.value.tvSeriesState.tvId)
                .collect {
                    reduce {
                        state.value = state.value.copy(
                            tvSeriesState = state.value.tvSeriesState.copy(
                                isAdded = it
                            )
                        )
                        state
                    }
                    postSideEffect(sideEffect = TvSeriesDetailsSideEffects.UpdateIsAddedState(it))
                }
        }
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

    fun addTvSeries() = intent {
        state.value.tvSeriesState.tvDetails?.let {
            addMovieToPlaylistUseCase.insertMovieToPlaylist(
                movie = it,
                playlistId = state.value.playlistState.playlists.find {
                    it.name == context.getString(R.string.favourite_tv_series)
                }?.playlistId ?: 0
            )
        }
    }

}