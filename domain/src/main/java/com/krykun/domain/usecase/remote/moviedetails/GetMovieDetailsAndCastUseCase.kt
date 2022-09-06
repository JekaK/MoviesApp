package com.krykun.domain.usecase.remote.moviedetails

import com.krykun.domain.model.remote.moviecastdetails.CastDetails
import com.krykun.domain.model.remote.moviedetails.MovieDetails
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetMovieDetailsAndCastUseCase @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getMovieCastUseCase: GetMovieCastDetailsUseCase
) {

    suspend fun getMovieDetailsAndCast(movieId: Int, callback: (Result<MovieDetails>?) -> Unit) =
        coroutineScope {
            var castResult: Result<CastDetails>? = null

            var result: Result<MovieDetails>? = null

            awaitAll(async {
                castResult = getMovieCastUseCase.getMovieCastDetails(movieId = movieId)
            }, async {
                result = getMovieDetailsUseCase.getMovieDetail(movieId = movieId)
            })

            callback(if (result?.isSuccess == true && castResult?.isSuccess == true) {
                val verifiedResponse = result?.map {
                    it.copy(
                        cast = castResult?.getOrNull()
                    )
                }
                verifiedResponse
            } else {
                null
            })
        }
}