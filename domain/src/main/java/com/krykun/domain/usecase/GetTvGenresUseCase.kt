package com.krykun.domain.usecase

import com.krykun.domain.repositories.MoviesRemoteRepo
import javax.inject.Inject

class GetTvGenresUseCase @Inject constructor(private val moviesRemoteRepo: MoviesRemoteRepo) {

    suspend fun getTvGenres() = moviesRemoteRepo.getTvGenres()
}