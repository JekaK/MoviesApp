package com.krykun.domain.usecase

import com.krykun.domain.repositories.MoviesRemoteRepo
import javax.inject.Inject

class GetDiscoverMoviesUseCase @Inject constructor(
    private val moviesRemoteRepo: MoviesRemoteRepo
) {

    fun getMovies(
        country: String? = null,
        language: String? = null,
        category: String? = null
    ) = moviesRemoteRepo.getDiscoverMovies(
        country = country,
        language = language,
        category = category
    )
}