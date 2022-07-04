package com.krykun.movieapp.feature.discover.data

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScreenCoordinates(
    val xFraction: Float,
    val yFraction: Float
) : Parcelable

class ScreenCoordinatesType : NavType<ScreenCoordinates>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): ScreenCoordinates? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): ScreenCoordinates {
        return Gson().fromJson(value, ScreenCoordinates::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: ScreenCoordinates) {
        bundle.putParcelable(key, value)
    }
}