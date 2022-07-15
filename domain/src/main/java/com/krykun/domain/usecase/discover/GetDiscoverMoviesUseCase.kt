package com.krykun.domain.usecase.discover

import com.krykun.domain.model.Genre
import com.krykun.domain.repositories.MoviesRemoteRepo
import javax.inject.Inject

class GetDiscoverMoviesUseCase @Inject constructor(
    private val moviesRemoteRepo: MoviesRemoteRepo
) {

    fun getMovies(
        country: String? = null,
        language: String? = null,
        category: String? = null,
        genres: List<Genre>
    ) = moviesRemoteRepo.getDiscoverMovies(
        country = country,
        language = language,
        category = category,
        genres = genres
    )
}