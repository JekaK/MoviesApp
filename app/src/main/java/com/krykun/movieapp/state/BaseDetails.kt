package com.krykun.movieapp.state


abstract class BaseDetails<T> {
    abstract var id: Int
    abstract var state: DetailsState
    abstract var isAdded: Boolean
    abstract var details:T?
}

enum class DetailsState {
    DEFAULT,
    LOADING,
    ERROR
}