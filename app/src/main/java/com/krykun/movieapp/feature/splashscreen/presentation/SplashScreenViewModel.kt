package com.krykun.movieapp.feature.splashscreen.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.krykun.domain.usecase.GetMovieGenresUseCase
import com.krykun.domain.usecase.GetTvGenresUseCase
import com.krykun.movieapp.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    appState: MutableStateFlow<AppState>,
    private val getMovieGenresUseCase: GetMovieGenresUseCase,
    private val getTvGenresUseCase: GetTvGenresUseCase
) : ViewModel(), ContainerHost<MutableStateFlow<AppState>, SplashScreenSideEffect> {

    private val splashDelay = 1000L

    override val container = container<MutableStateFlow<AppState>, SplashScreenSideEffect>(appState)

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