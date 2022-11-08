package com.krykun.movieapp.di

import com.krykun.domain.usecase.remote.discover.GetDiscoverMoviesUseCase
import com.krykun.movieapp.feature.discover.middleware.DiscoverMoviesMiddleware
import com.krykun.movieapp.feature.discover.middleware.IDiscoverMoviesMiddleware
import com.krykun.movieapp.feature.discover.middleware.InitDiscoverFeatureMiddleware
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MiddlewareModule {

    @Singleton
    @Provides
    fun provideDiscoverMoviesMiddleware(
        getDiscoverMoviesUseCase: GetDiscoverMoviesUseCase
    ) = DiscoverMoviesMiddleware(
        getDiscoverMoviesUseCase = getDiscoverMoviesUseCase
    )

    @Singleton
    @Provides
    fun provideFeatureMiddlewares(discoverMoviesMiddleware: DiscoverMoviesMiddleware) =
        arrayListOf<IDiscoverMoviesMiddleware>(discoverMoviesMiddleware)

    @Singleton
    @Provides
    fun provideInitDiscoverFeatureMiddleware(
        middlewareList: ArrayList<IDiscoverMoviesMiddleware>
    ) = InitDiscoverFeatureMiddleware(middlewareList)
}