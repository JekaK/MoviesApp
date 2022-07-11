package com.krykun.movieapp.ext

fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return (1 - fraction) * start + fraction * stop
}

fun Double.round(decimals: Int = 2): Double = "%.${decimals}f".format(this).toDouble()