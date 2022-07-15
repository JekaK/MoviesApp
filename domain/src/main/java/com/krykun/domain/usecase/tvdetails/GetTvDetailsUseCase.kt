package com.krykun.domain.usecase.tvdetails

import com.krykun.domain.repositories.MoviesRemoteRepo
import javax.inject.Inject

class GetTvDetailsUseCase @Inject constructor(private val moviesRemoteRepo: MoviesRemoteRepo) {

    suspend fun getTvDetails(tvId: Int) = moviesRemoteRepo.getTvDetails(tvId = tvId)
}