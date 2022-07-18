package com.krykun.domain.usecase.remote.search

import com.krykun.domain.model.remote.Genre
import com.krykun.domain.repositories.remote.MoviesRemoteRepo
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