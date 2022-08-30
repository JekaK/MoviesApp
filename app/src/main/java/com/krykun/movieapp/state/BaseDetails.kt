package com.krykun.movieapp.state

abstract class BaseDetails<T> {
    abstract var id: Int
    abstract var state: DetailsState
    abstract var isAdded: Boolean
    abstract var details:T?
    abstract var currentPageIndex: Int
    abstract var scrollOffset: Int
    abstract var lastSavedPage: Int
    abstract var loadingState: LoadingState
}

enum class DetailsState {
    DEFAULT,
    LOADING,
    ERROR
}

enum class LoadingState {
    LOADING,
    STATIONARY
}