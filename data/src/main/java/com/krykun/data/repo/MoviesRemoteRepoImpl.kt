package com.krykun.data.repo

import androidx.paging.PagingData
import androidx.paging.map
import com.krykun.data.datasource.MoviesRemoteDataSource
import com.krykun.data.mappers.DiscoverMoviesMapper.toMovieDiscoverItem
import com.krykun.data.mappers.GenresMapper.toGenre
import com.krykun.data.mappers.MovieDetailMapper.toCastDetails
import com.krykun.data.mappers.MovieDetailMapper.toMovieDetails
import com.krykun.data.mappers.MoviesMapper.toMovie
import com.krykun.data.mappers.SearchMapper.toSearchItem
import com.krykun.data.mappers.TvDetailsMapper.toTvCastDetails
import com.krykun.data.mappers.TvDetailsMapper.toTvDetails
import com.krykun.domain.model.Genre
import com.krykun.domain.model.MovieDiscoverItem
import com.krykun.domain.model.moviecastdetails.CastDetails
import com.krykun.domain.model.moviedetails.MovieDetails
import com.krykun.domain.model.movies.Movie
import com.krykun.domain.model.search.SearchItem
import com.krykun.domain.model.tvcastdetails.TvCastDetails
import com.krykun.domain.model.tvdetails.TvDetails
import com.krykun.domain.repositories.MoviesRemoteRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MoviesRemoteRepoImpl @Inject constructor(
    private val remoteDataSource: MoviesRemoteDataSource
) : MoviesRemoteRepo {

    override fun getDiscoverMovies(
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
                movieItem.toMovieDiscoverItem(genres)
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

    override suspend fun getTvGenres(): Result<List<Genre>> {
        return remoteDataSource.getTvGenres().map {
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

    override suspend fun getTvDetails(tvId: Int): Result<TvDetails> {
        return remoteDataSource.getTvDetails(tvId).map {
            it.toTvDetails()
        }
    }

    override suspend fun getMovieCastDetails(movieId: Int): Result<CastDetails> {
        return remoteDataSource.getMovieCastDetails(movieId).map {
            it.toCastDetails()
        }
    }

    override suspend fun getTvSeriesCastDetail(tvId: Int): Result<TvCastDetails> {
        return remoteDataSource.getTvCastDetails(tvId).map {
            it.toTvCastDetails()
        }
    }

    override fun getTrendingMovies(
        genres: List<Genre>,
    ): Flow<PagingData<Movie>> {
        return remoteDataSource.getTrendingMovies().map {
            it.map { movieItem ->
                movieItem.toMovie(genres)
            }
        }
    }

    override fun getPopularMovies(genres: List<Genre>): Flow<PagingData<Movie>> {
        return remoteDataSource.getPopularMovies().map {
            it.map { movieItem ->
                movieItem.toMovie(genres)
            }
        }
    }

    override fun getTopRatedMovies(genres: List<Genre>): Flow<PagingData<Movie>> {
        return remoteDataSource.getTopRatedMovies().map {
            it.map { movieItem ->
                movieItem.toMovie(genres)
            }
        }
    }

    override fun makeSearch(query: String, genres: List<Genre>): Flow<PagingData<SearchItem>> {
        return remoteDataSource.makeSearch(query).map {
            it.map { searchItem ->
                searchItem.toSearchItem(genres)
            }
        }
    }
}