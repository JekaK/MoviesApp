package com.krykun.movieapp.feature.person.presentation

import com.krykun.domain.model.remote.persondetails.PersonDetails

data class PersonDetailsState(
    val id: Int = -1,
    val personDetails: PersonDetails? = null,
    val personLoadingState: PersonLoadingState = PersonLoadingState.DEFAULT,
    val selectedPersonTab: PersonTabs = PersonTabs.FILMOGRAPHY
)

enum class PersonLoadingState {
    DEFAULT,
    LOADING,
    ERROR
}

enum class PersonTabs {
    FILMOGRAPHY,
    PRODUCTION
}
