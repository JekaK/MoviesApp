package com.krykun.data.mappers.remote

import com.krykun.data.model.remote.genre.Genre

object GenresMapper {
    fun Genre.toGenre(): com.krykun.domain.model.remote.Genre {
        return com.krykun.domain.model.remote.Genre(
            id = id ?: 0,
            name = name ?: ""
        )
    }

    fun Int.toGenre(genreList: List<com.krykun.domain.model.remote.Genre>): String {
        return genreList.find {
            it.id == this
        }?.name ?: ""
    }
}