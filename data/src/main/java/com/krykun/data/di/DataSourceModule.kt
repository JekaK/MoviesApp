package com.krykun.data.di

import com.krykun.data.api.ApiService
import com.krykun.data.dao.MovieDao
import com.krykun.data.dao.PlaylistDao
import com.krykun.data.datasource.local.MoviesLocalDataSource
import com.krykun.data.datasource.local.PlaylistLocalDataSource
import com.krykun.data.datasource.local.impl.MoviesLocalDataSourceImpl
import com.krykun.data.datasource.local.impl.PlaylistLocalDataSourceImpl
import com.krykun.data.datasource.remote.MoviesRemoteDataSource
import com.krykun.data.datasource.remote.impl.MoviesRemoteDataSourceImpl
import com.krykun.data.repo.MoviesLocalRepoImpl
import com.krykun.data.repo.MoviesRemoteRepoImpl
import com.krykun.data.repo.PlaylistLocalRepoImpl
import com.krykun.domain.repositories.local.MoviesLocalRepo
import com.krykun.domain.repositories.local.PlaylistsLocalRepo
import com.krykun.domain.repositories.remote.MoviesRemoteRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    /* Providing a dependency for the `MoviesRemoteDataSource` interface. */
    @Provides
    @Singleton
    fun provideRemoteDataSource(apiService: ApiService): MoviesRemoteDataSource =
        MoviesRemoteDataSourceImpl(apiService)

    /* Providing a dependency for the `MoviesLocalDataSource` interface. */
    @Provides
    @Singleton
    fun provideMoviesLocalDataSource(movieDao: MovieDao): MoviesLocalDataSource =
        MoviesLocalDataSourceImpl(movieDao)

    /* Providing a dependency for the `PlaylistLocalDataSource` interface. */
    @Provides
    @Singleton
    fun providePlaylistsLocalDataSource(
        playlistDao: PlaylistDao,
        movieDao: MovieDao
    ): PlaylistLocalDataSource = PlaylistLocalDataSourceImpl(playlistDao, movieDao)

    /* Providing a dependency for the `MoviesRemoteRepo` interface. */
    @Provides
    @Singleton
    fun provideMoviesRemoteRepo(remoteDataSource: MoviesRemoteDataSource): MoviesRemoteRepo =
        MoviesRemoteRepoImpl(remoteDataSource)

    /* Providing a dependency for the `MoviesLocalRepo` interface. */
    @Provides
    @Singleton
    fun provideMoviesLocalRepo(moviesLocalDataSource: MoviesLocalDataSource): MoviesLocalRepo =
        MoviesLocalRepoImpl(moviesLocalDataSource)

    /* Providing a dependency for the `PlaylistsLocalRepo` interface. */
    @Provides
    @Singleton
    fun providePlaylistsLocalRepo(playlistLocalDataSource: PlaylistLocalDataSource): PlaylistsLocalRepo =
        PlaylistLocalRepoImpl(playlistLocalDataSource)
}