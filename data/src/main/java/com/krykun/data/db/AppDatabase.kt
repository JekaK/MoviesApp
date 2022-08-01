package com.krykun.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.krykun.data.dao.MovieDao
import com.krykun.data.dao.PlaylistDao
import com.krykun.data.model.local.Movie
import com.krykun.data.model.local.Playlist
import com.krykun.data.model.local.PlaylistMovieCrossRef

@Database(
    entities = [Playlist::class, Movie::class, PlaylistMovieCrossRef::class],
    version = 3,
)
/* It's an abstract class that extends RoomDatabase and provides abstract methods for each DAO that the
app uses */
abstract class AppDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
    abstract fun movieDao(): MovieDao
}