package com.krykun.movieapp.feature.person.presentation

import com.krykun.domain.model.persondetails.PersonDetails
import com.krykun.movieapp.feature.person.view.PersonTabs

sealed class PersonSideEffects {
    data class ShowPersonDetailsData(val personDetails: PersonDetails?) : PersonSideEffects()
    object ShowLoadingState : PersonSideEffects()
    object ShowErrorState : PersonSideEffects()

}
