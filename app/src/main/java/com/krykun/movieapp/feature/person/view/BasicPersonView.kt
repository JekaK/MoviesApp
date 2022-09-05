package com.krykun.movieapp.feature.person.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.krykun.movieapp.R
import com.krykun.movieapp.ext.header
import com.krykun.movieapp.feature.person.presentation.PersonTabs
import com.krykun.movieapp.feature.person.presentation.PersonViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BasicPersonView(
    navHostController: NavHostController,
    viewModel: PersonViewModel
) {
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
                                personDetails = viewModel.personDetails,
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
                                        isSelected = viewModel.selectedPersonTab.value == PersonTabs.FILMOGRAPHY,
                                        text = stringResource(id = R.string.filmography)
                                    ) {
                                        viewModel.setPersonTabSelected(PersonTabs.FILMOGRAPHY)
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))

                                    PersonTab(
                                        modifier = Modifier
                                            .weight(1f),
                                        isSelected = viewModel.selectedPersonTab.value == PersonTabs.PRODUCTION,
                                        text = stringResource(id = R.string.production)
                                    ) {
                                        viewModel.setPersonTabSelected(PersonTabs.PRODUCTION)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }

                    when (viewModel.selectedPersonTab.value) {
                        PersonTabs.FILMOGRAPHY -> {
                            if (viewModel.personDetails.value?.personCombinedCredits?.cast?.isNotEmpty() == true) {
                                itemsIndexed(
                                    viewModel.personDetails.value?.personCombinedCredits?.cast
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
                            if (viewModel.personDetails.value?.personCombinedCredits?.crew?.isNotEmpty() == true) {
                                itemsIndexed(
                                    viewModel.personDetails.value?.personCombinedCredits?.crew
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