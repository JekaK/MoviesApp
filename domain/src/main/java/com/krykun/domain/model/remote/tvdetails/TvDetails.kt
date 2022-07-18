package com.krykun.domain.model.remote.tvdetails

import com.krykun.domain.model.remote.tvcastdetails.TvCastDetails

data class TvDetails(
    val adult: Boolean? = null,
    val backdropPath: String? = null,
    val createdBy: List<CreatedBy?>? = null,
    val episodeRunTime: List<Int?>? = null,
    val firstAirDate: String? = null,
    val genres: List<Genre?>? = null,
    val homepage: String? = null,
    val id: Int? = null,
    val inProduction: Boolean? = null,
    val languages: List<String?>? = null,
    val lastAirDate: String? = null,
    val lastEpisodeToAir: LastEpisodeToAir? = null,
    val name: String? = null,
    val networks: List<Network?>? = null,
    val nextEpisodeToAir: Any? = null,
    val numberOfEpisodes: Int? = null,
    val numberOfSeasons: Int? = null,
    val originCountry: List<String?>? = null,
    val originalLanguage: String? = null,
    val originalName: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    val posterPath: String? = null,
    val productionCompanies: List<ProductionCompany?>? = null,
    val productionCountries: List<ProductionCountry?>? = null,
    val seasons: List<Season?>? = null,
    val spokenLanguages: List<SpokenLanguage?>? = null,
    val status: String? = null,
    val tagline: String? = null,
    val type: String? = null,
    val voteAverage: Double? = null,
    val voteCount: Int? = null,
    val cast: TvCastDetails? = null
)