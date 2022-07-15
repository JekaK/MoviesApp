package com.krykun.movieapp.feature.splashscreen.presentation

import androidx.compose.runtime.mutableStateOf
import com.krykun.domain.usecase.moviedetails.GetMovieGenresUseCase
import com.krykun.domain.usecase.tvdetails.GetTvGenresUseCase
import com.krykun.movieapp.base.BaseViewModel
import com.krykun.movieapp.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    appState: MutableStateFlow<AppState>,
    private val getMovieGenresUseCase: GetMovieGenresUseCase,
    private val getTvGenresUseCase: GetTvGenresUseCase
) : BaseViewModel<SplashScreenSideEffect>(appState) {

    private val splashDelay = 1000L

    val startAnimFlag = mutableStateOf(false)

    init {
        setScreenOpen()
    }

    private fun setScreenOpen() = intent {
        reduce {
            state.value =
                state.value.copy(splashScreenState = state.value.splashScreenState.copy(isScreenOpen = true))
            state
        }
    }

    fun makeInitialDelay() = intent {
        val response = getMovieGenresUseCase.getMovieGenres()
        val tvResponse = getTvGenresUseCase.getTvGenres()
        if (response.isSuccess &&
            tvResponse.isSuccess
        ) {
            reduce {
                state.value = state.value.copy(
                    baseMoviesState = state.value.baseMoviesState.copy(
                        genres = (response.getOrNull() ?: listOf()) +
                                (tvResponse.getOrNull() ?: listOf()),
                    )
                )
                state
            }
            delay(splashDelay)
            postSideEffect(SplashScreenSideEffect.MoveToNextScreen)
        }
    }
}