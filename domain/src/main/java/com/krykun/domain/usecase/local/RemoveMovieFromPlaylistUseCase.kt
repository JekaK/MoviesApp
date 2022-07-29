package com.krykun.domain.usecase.local

import com.krykun.domain.model.remote.moviedetails.MovieDetails
import com.krykun.domain.model.remote.tvdetails.TvDetails
import com.krykun.domain.repositories.local.MoviesLocalRepo
import javax.inject.Inject

class RemoveMovieFromPlaylistUseCase @Inject constructor(
    private val moviesLocalRepo: MoviesLocalRepo
) {

    suspend fun removeMovieFromPlaylist(movie: MovieDetails, playlistId: Long) =
        moviesLocalRepo.removeMovieFromPlaylist(movie, playlistId)

    suspend fun removeMovieFromPlaylist(movie: TvDetails, playlistId: Long) =
        moviesLocalRepo.removeMovieFromPlaylist(movie, playlistId)
}