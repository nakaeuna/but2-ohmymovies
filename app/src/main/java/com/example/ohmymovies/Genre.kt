package com.example.ohmymovies

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class Genre(
    val id: Int = 0,
    val name: String
) : Parcelable
