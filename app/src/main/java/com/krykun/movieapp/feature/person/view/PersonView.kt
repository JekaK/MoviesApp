package com.krykun.movieapp.feature.person.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.krykun.domain.model.remote.persondetails.PersonDetails
import com.krykun.movieapp.R
import com.krykun.movieapp.ext.header
import com.krykun.movieapp.feature.person.presentation.PersonSideEffects
import com.krykun.movieapp.feature.person.presentation.PersonViewModel
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PersonView(
    viewModel: PersonViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val personDetails = remember { mutableStateOf<PersonDetails?>(null) }
    val personDetailsState = remember { mutableStateOf(PersonDetailsState.LOADING) }
    val selectedPersonTab = remember {
        mutableStateOf(PersonTabs.FILMOGRAPHY)
    }

    viewModel.collectSideEffect {
        handleSideEffects(
            sideEffects = it,
            personDetails = personDetails,
            personDetailsState = personDetailsState,
            selectedPersonTab = selectedPersonTab
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        BackBtn(navHostController)
        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null
        ) {
            BoxWithConstraints {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize(),
                    columns = GridCells.Adaptive(minSize = 128.dp),
                ) {
                    header {
                        Column() {
                            MainPersonInfo(
                                personDetails = personDetails,
                                modifier = Modifier
                            )
                            Card(
                                elevation = 16.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .padding(
                                        start = 36.dp,
                                        end = 36.dp,
                                    ),
                                shape = CircleShape,
                                backgroundColor = Color.White,
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    PersonTab(
                                        modifier = Modifier
                                            .weight(1f),
                                        isSelected = selectedPersonTab.value == PersonTabs.FILMOGRAPHY,
                                        text = stringResource(id = R.string.filmography)
                                    ) {
                                        viewModel.setPersonTabSelected(PersonTabs.FILMOGRAPHY)
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))

                                    PersonTab(
                                        modifier = Modifier
                                            .weight(1f),
                                        isSelected = selectedPersonTab.value == PersonTabs.PRODUCTION,
                                        text = stringResource(id = R.string.production)
                                    ) {
                                        viewModel.setPersonTabSelected(PersonTabs.PRODUCTION)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    when (selectedPersonTab.value) {
                        PersonTabs.FILMOGRAPHY -> {
                            if (personDetails.value?.personCombinedCredits?.cast?.isNotEmpty() == true) {
                                itemsIndexed(
                                    personDetails.value?.personCombinedCredits?.cast
                                        ?: listOf()
                                ) { index, item ->
                                    CastView(castItem = item)
                                }
                            } else {
                                header {
                                    EmptyView()
                                }
                            }
                        }
                        PersonTabs.PRODUCTION -> {
                            if (personDetails.value?.personCombinedCredits?.crew?.isNotEmpty() == true) {
                                itemsIndexed(
                                    personDetails.value?.personCombinedCredits?.crew
                                        ?: listOf()
                                ) { index, item ->
                                    CrewView(crewItem = item)
                                }
                            } else {
                                header {
                                    EmptyView()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class PersonTabs {
    FILMOGRAPHY,
    PRODUCTION
}

@Composable
private fun BackBtn(navHostController: NavHostController) {
    Row(modifier = Modifier.zIndex(1f)) {
        Spacer(modifier = Modifier.width(24.dp))
        Column {
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .clickable {
                        navHostController.popBackStack()
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = ""
                )
            }
        }
    }
}

private fun handleSideEffects(
    sideEffects: PersonSideEffects,
    personDetails: MutableState<PersonDetails?>,
    personDetailsState: MutableState<PersonDetailsState>,
    selectedPersonTab: MutableState<PersonTabs>
) {
    when (sideEffects) {
        is PersonSideEffects.ShowPersonDetailsData -> {
            personDetailsState.value = PersonDetailsState.DEFAULT
            personDetails.value = sideEffects.personDetails
        }
        is PersonSideEffects.ShowErrorState -> {
            personDetailsState.value = PersonDetailsState.ERROR
        }
        is PersonSideEffects.ShowLoadingState -> {
            personDetailsState.value = PersonDetailsState.LOADING
        }
        is PersonSideEffects.SelectCastTab -> {
            selectedPersonTab.value = PersonTabs.FILMOGRAPHY
        }
        is PersonSideEffects.SelectCrewTab -> {
            selectedPersonTab.value = PersonTabs.PRODUCTION
        }
    }
}

enum class PersonDetailsState {
    LOADING,
    DEFAULT,
    ERROR
}

