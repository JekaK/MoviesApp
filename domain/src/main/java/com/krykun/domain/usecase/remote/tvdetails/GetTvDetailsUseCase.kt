package com.krykun.domain.usecase.remote.tvdetails

import com.krykun.domain.repositories.remote.MoviesRemoteRepo
import javax.inject.Inject

class GetTvDetailsUseCase @Inject constructor(private val moviesRemoteRepo: MoviesRemoteRepo) {

    suspend fun getTvDetails(tvId: Int) = moviesRemoteRepo.getTvDetails(tvId = tvId)
}