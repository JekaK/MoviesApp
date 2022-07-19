package com.krykun.data.model.local

import androidx.room.*

@Entity
data class Playlist(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long = 0,
    val playlistName: String
)

@Entity
data class Movie(
    @PrimaryKey val movieId: Long,
    val name: String,
    val poster: String,
    val type: String
)

@Entity(primaryKeys = ["playlistId", "movieId"])
data class PlaylistMovieCrossRef(
    val playlistId: Long,
    val movieId: Long
)

data class PlaylistWithMovies(
    @Embedded val playlist: Playlist,
    @Relation(
        parentColumn = "playlistId",
        entity = Movie::class,
        entityColumn = "movieId",
        associateBy = Junction(PlaylistMovieCrossRef::class)
    )
    val movies: List<Movie>
)
