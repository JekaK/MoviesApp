package com.krykun.movieapp.feature.person.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.krykun.domain.model.remote.persondetails.PersonDetails
import com.krykun.movieapp.feature.person.presentation.PersonLoadingState
import com.krykun.movieapp.feature.person.presentation.PersonSideEffects
import com.krykun.movieapp.feature.person.presentation.PersonTabs
import com.krykun.movieapp.feature.person.presentation.PersonViewModel
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun PersonView(
    viewModel: PersonViewModel = hiltViewModel(),
    navHostController: NavHostController
) {

    viewModel.collectSideEffect {
        handleSideEffects(
            sideEffects = it,
            viewModel = viewModel
        )
    }
    BasicPersonView(
        navHostController = navHostController,
        viewModel = viewModel
    )
}


private fun handleSideEffects(
    sideEffects: PersonSideEffects,
    viewModel: PersonViewModel
) {
    when (sideEffects) {
        is PersonSideEffects.ShowPersonDetailsData -> {
            viewModel.personDetailsState.value = PersonLoadingState.DEFAULT
            viewModel.personDetails.value = sideEffects.personDetails
        }
        is PersonSideEffects.ShowErrorState -> {
            viewModel.personDetailsState.value = PersonLoadingState.ERROR
        }
        is PersonSideEffects.ShowLoadingState -> {
            viewModel.personDetailsState.value = PersonLoadingState.LOADING
        }
        is PersonSideEffects.SelectCastTab -> {
            viewModel.selectedPersonTab.value = PersonTabs.FILMOGRAPHY
        }
        is PersonSideEffects.SelectCrewTab -> {
            viewModel.selectedPersonTab.value = PersonTabs.PRODUCTION
        }
    }
}
