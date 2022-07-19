package com.krykun.data.di

import android.content.Context
import androidx.room.Room
import com.krykun.data.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {

    @Singleton
    @Provides
    fun provideDb(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java, "movies-database"
    ).build()

    @Singleton
    @Provides
    fun provideMovieDao(appDatabase: AppDatabase) = appDatabase.movieDao()

    @Singleton
    @Provides
    fun providePlaylistDao(appDatabase: AppDatabase) = appDatabase.playlistDao()
}