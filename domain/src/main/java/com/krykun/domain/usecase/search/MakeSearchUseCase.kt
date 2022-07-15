package com.krykun.domain.usecase.search

import com.krykun.domain.model.Genre
import com.krykun.domain.repositories.MoviesRemoteRepo
import javax.inject.Inject

class MakeSearchUseCase @Inject constructor(
    private val moviesRemoteRepo: MoviesRemoteRepo
) {

    fun makeSearch(
        query: String,
        genres: List<Genre>
    ) = moviesRemoteRepo.makeSearch(
        query = query,
        genres = genres
    )
}