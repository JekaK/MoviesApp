package com.krykun.data.mappers.local

import com.krykun.data.mappers.local.MoviesMapper.toMovie
import com.krykun.data.model.local.PlaylistWithMovies
import com.krykun.domain.model.local.Movie
import com.krykun.domain.model.local.Playlist

object PlaylistMapper {

    fun Playlist.toPlaylist(): com.krykun.data.model.local.Playlist {
        return com.krykun.data.model.local.Playlist(
            playlistId = playlistId,
            playlistName = name
        )
    }

    fun PlaylistWithMovies.toPlaylist(): Playlist {
        return Playlist(
            playlistId = playlist.playlistId,
            name = playlist.playlistName,
            movieList = movies.map {
                it.toMovie()
            }
        )
    }
}