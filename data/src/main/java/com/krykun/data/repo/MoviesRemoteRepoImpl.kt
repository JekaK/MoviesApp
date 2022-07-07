package com.krykun.data.repo

import androidx.paging.PagingData
import androidx.paging.map
import com.krykun.data.datasource.MoviesRemoteDataSource
import com.krykun.data.mappers.CastDetailsMapper.toCastDetails
import com.krykun.data.mappers.DiscoverMoviesMapper.toMovieDiscoverItem
import com.krykun.data.mappers.GenresMapper.toGenre
import com.krykun.data.mappers.MovieDetailMapper.toMovieDetails
import com.krykun.domain.model.Genre
import com.krykun.domain.model.MovieDiscoverItem
import com.krykun.domain.model.castdetails.CastDetails
import com.krykun.domain.model.moviedetails.MovieDetails
import com.krykun.domain.repositories.MoviesRemoteRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MoviesRemoteRepoImpl @Inject constructor(
    private val remoteDataSource: MoviesRemoteDataSource
) : MoviesRemoteRepo {

    override fun getUpcomingMovies(
        country: String?,
        language: String?,
        category: String?,
        genres: List<Genre>
    ): Flow<PagingData<MovieDiscoverItem>> {
        return remoteDataSource.getUpcomingMovies(
            country = country,
            language = language,
            category = category
        ).map {
            it.map { movieItem ->
                movieItem.toMovieDiscoverItem()
                    .copy(
                        mappedGenreIds = movieItem.genreIds
                            ?.map {
                                it?.toGenre(genres) ?: ""
                            } ?: listOf()
                    )
            }
        }
    }

    override suspend fun getGenres(): Result<List<Genre>> {
        return remoteDataSource.getGenres().map {
            it.map {
                it.toGenre()
            }
        }
    }

    override suspend fun getMovieDetails(movieId: Int): Result<MovieDetails> {
        return remoteDataSource.getMovieDetails(movieId).map {
            it.toMovieDetails()
        }
    }

    override suspend fun getCastDetails(movieId: Int): Result<CastDetails> {
        return remoteDataSource.getCastDetails(movieId).map {
            it.toCastDetails()
        }
    }
}