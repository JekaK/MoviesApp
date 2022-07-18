package com.krykun.data.repo

import androidx.paging.PagingData
import androidx.paging.map
import com.krykun.data.datasource.remote.MoviesRemoteDataSource
import com.krykun.data.mappers.remote.DiscoverMoviesMapper.toMovieDiscoverItem
import com.krykun.data.mappers.remote.GenresMapper.toGenre
import com.krykun.data.mappers.remote.MovieDetailMapper.toCastDetails
import com.krykun.data.mappers.remote.MovieDetailMapper.toMovieDetails
import com.krykun.data.mappers.remote.MoviesMapper.toMovie
import com.krykun.data.mappers.remote.PersonCombinedCreditsMapper.toPersonCombinedCredits
import com.krykun.data.mappers.remote.PersonDetailsMapper.toPersonDetails
import com.krykun.data.mappers.remote.SearchMapper.toSearchItem
import com.krykun.data.mappers.remote.TvDetailsMapper.toTvCastDetails
import com.krykun.data.mappers.remote.TvDetailsMapper.toTvDetails
import com.krykun.domain.model.remote.personcombinedcredits.PersonCombinedCredits
import com.krykun.domain.model.remote.Genre
import com.krykun.domain.model.remote.MovieDiscoverItem
import com.krykun.domain.model.remote.moviecastdetails.CastDetails
import com.krykun.domain.model.remote.moviedetails.MovieDetails
import com.krykun.domain.model.remote.movies.Movie
import com.krykun.domain.model.remote.persondetails.PersonDetails
import com.krykun.domain.model.remote.search.SearchItem
import com.krykun.domain.model.remote.tvcastdetails.TvCastDetails
import com.krykun.domain.model.remote.tvdetails.TvDetails
import com.krykun.domain.repositories.remote.MoviesRemoteRepo
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

    override suspend fun getPersonDetails(personId: Int): Result<PersonDetails> {
        return remoteDataSource.getPersonDetails(personId).map {
            it.toPersonDetails()
        }
    }

    override suspend fun getPersonCombinedCredits(
        personId: Int,
        genres: List<Genre>
    ): Result<PersonCombinedCredits> {
        return remoteDataSource.getPersonCombinedCredits(personId).map {
            it.toPersonCombinedCredits(genres)
        }
    }
}