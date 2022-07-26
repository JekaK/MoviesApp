package com.krykun.movieapp.feature.playlistselect.presentation

import com.krykun.domain.model.local.Playlist
import com.krykun.domain.model.remote.moviedetails.MovieDetails
import com.krykun.domain.model.remote.tvdetails.TvDetails

data class PlaylistSelectState(
    val playlists: List<MappedPlaylist> = listOf(),
    val movieDetails: MovieDetails = MovieDetails(),
    val tvDetails: TvDetails = TvDetails()
)

data class MappedPlaylist(
    val playlist: Playlist = Playlist(),
    val isMovieInPlaylist: Boolean = false
)