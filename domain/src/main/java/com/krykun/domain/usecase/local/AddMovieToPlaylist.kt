package com.krykun.domain.usecase.local

import com.krykun.domain.model.remote.moviedetails.MovieDetails
import com.krykun.domain.model.remote.tvdetails.TvDetails
import com.krykun.domain.repositories.local.MoviesLocalRepo
import javax.inject.Inject

class AddMovieToPlaylist @Inject constructor(
    private val moviesLocalRepo: MoviesLocalRepo
) {

    fun insertMovieToPlaylist(movie: MovieDetails, playlistId: Long) =
        moviesLocalRepo.insertMovie(movie, playlistId)

    fun insertMovieToPlaylist(movie: TvDetails, playlistId: Long) =
        moviesLocalRepo.insertMovie(movie, playlistId)
}