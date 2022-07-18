package com.krykun.movieapp.feature.person.presentation

import com.krykun.domain.usecase.remote.persondetails.GetPersonCombinedCreditsUseCase
import com.krykun.domain.usecase.remote.persondetails.GetPersonDetailsUseCase
import com.krykun.movieapp.base.BaseViewModel
import com.krykun.movieapp.ext.takeWhenChanged
import com.krykun.movieapp.feature.person.view.PersonTabs
import com.krykun.movieapp.state.AppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor(
    appState: MutableStateFlow<AppState>,
    private val getPersonDetailsUseCase: GetPersonDetailsUseCase,
    private val getPersonCombinedCreditsUseCase: GetPersonCombinedCreditsUseCase,
) : BaseViewModel<PersonSideEffects>(appState) {

    init {
        loadPersonDetails()
    }

    fun subscribeToState() = container.stateFlow.value.takeWhenChanged {
        it.personState
    }

    private fun loadPersonDetails() = intent {
        postSideEffect(PersonSideEffects.ShowLoadingState)

        val result = getPersonDetailsUseCase.getPersonDetails(state.value.personState.id)
        val castResult = getPersonCombinedCreditsUseCase.getPersonCombinedCredits(
            state.value.personState.id,
            state.value.baseMoviesState.genres
        )
        if (result.isSuccess && castResult.isSuccess) {
            val verifiedResponse = result.map {
                it.copy(personCombinedCredits = castResult.getOrNull())
            }
            reduce {
                state.value = state.value.copy(
                    personState = PersonDetailsState(
                        personDetails = verifiedResponse.getOrNull()
                    )
                )
                state
            }
            postSideEffect(PersonSideEffects.ShowPersonDetailsData(verifiedResponse.getOrNull()))
        } else {
            postSideEffect(PersonSideEffects.ShowErrorState)
        }
    }

    fun setPersonTabSelected(selectedPersonTab: PersonTabs) = intent {
        val selectedTab = when (selectedPersonTab) {
            PersonTabs.FILMOGRAPHY -> com.krykun.movieapp.feature.person.presentation.PersonTabs.FILMOGRAPHY
            PersonTabs.PRODUCTION -> com.krykun.movieapp.feature.person.presentation.PersonTabs.PRODUCTION
        }
        reduce {
            state.value = state.value.copy(
                personState = state.value.personState.copy(
                    selectedPersonTab = selectedTab
                )
            )
            state
        }
    }
}