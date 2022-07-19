package com.krykun.domain.model.local


data class Playlist(
    val playlistId: Long = 0,
    val name: String,
    val movieList: List<Movie>
)

data class Movie(
    val movieId: Long,
    val name: String,
    val poster: String,
    val type: String
)