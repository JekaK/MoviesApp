package com.krykun.data.datasource.local.impl

import com.krykun.data.dao.MovieDao
import com.krykun.data.dao.PlaylistDao
import com.krykun.data.datasource.local.PlaylistLocalDataSource
import com.krykun.data.model.local.Playlist
import com.krykun.data.model.local.PlaylistMovieCrossRef
import com.krykun.data.model.local.PlaylistWithMovies
import kotlinx.coroutines.flow.Flow

class PlaylistLocalDataSourceImpl(
    private val playlistDao: PlaylistDao,
    private val movieDao: MovieDao
) : PlaylistLocalDataSource {
    override suspend fun insertPlaylist(playlist: Playlist) =
        playlistDao.insertPlaylist(playlist)

    override suspend fun insertPlaylistMovieCrossRef(crossRef: PlaylistMovieCrossRef) =
        playlistDao.insertPlaylistMovieCrossRef(crossRef)

    override fun getPlaylistsWithMoviesFlow(): Flow<List<PlaylistWithMovies>> {
        return playlistDao.getAllPlaylistsWithMoviesFlow()
    }

    override fun getPlaylistsWithMovies(): List<PlaylistWithMovies> {
        return playlistDao.getAllPlaylistsWithMovies()
    }

    override fun getPlaylistsWithMoviesById(playlistId: Long): Flow<PlaylistWithMovies> {
        return playlistDao.getPlaylistsWithMoviesById(playlistId)
    }

    override fun getAllPlaylistsWithMoviesByLimit(amount: Int): Flow<List<PlaylistWithMovies>> {
        return playlistDao.getAllPlaylistsWithMoviesByLimit(amount)
    }

    override suspend fun removePlaylist(playlistId: Long) {
        playlistDao.removePlaylist(playlistId)
        playlistDao.deleteAllPlaylistCrossRefById(playlistId)
        movieDao.getAllMovies().forEach {
            if (movieDao.searchInCrossRefForMovie(movieId = it.movieId.toInt()) == 0L) {
                movieDao.removeMovieFromPlaylistById(movieId = it.movieId)
            }
        }
    }
}