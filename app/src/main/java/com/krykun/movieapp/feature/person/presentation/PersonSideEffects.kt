package com.krykun.movieapp.feature.person.presentation

import com.krykun.domain.model.remote.persondetails.PersonDetails

sealed class PersonSideEffects {
    data class ShowPersonDetailsData(val personDetails: PersonDetails?) : PersonSideEffects()
    object ShowLoadingState : PersonSideEffects()
    object ShowErrorState : PersonSideEffects()

    object SelectCastTab : PersonSideEffects()
    object SelectCrewTab : PersonSideEffects()
}
