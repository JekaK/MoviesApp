package com.krykun.movieapp.feature.playlistselect.presentation

import com.krykun.domain.model.local.Playlist
import com.krykun.domain.model.remote.moviedetails.MovieDetails

data class PlaylistSelectState(
    val playlists: List<MappedPlaylist> = listOf(),
    val movieDetails: MovieDetails = MovieDetails()
)

data class MappedPlaylist(
    val playlist: Playlist = Playlist(),
    val isMovieInPlaylist: Boolean = false
)