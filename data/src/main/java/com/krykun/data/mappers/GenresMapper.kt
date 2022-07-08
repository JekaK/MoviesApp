package com.krykun.data.mappers

import com.krykun.data.model.genre.Genre

object GenresMapper {
    fun Genre.toGenre(): com.krykun.domain.model.Genre {
        return com.krykun.domain.model.Genre(
            id = id ?: 0,
            name = name ?: ""
        )
    }

    fun Int.toGenre(genreList: List<com.krykun.domain.model.Genre>): String {
        return genreList.find {
            it.id == this
        }?.name ?: ""
    }
}