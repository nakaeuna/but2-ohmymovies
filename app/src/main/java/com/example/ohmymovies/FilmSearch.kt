package com.example.ohmymovies

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@OptIn(ExperimentalSerializationApi::class)
@JsonIgnoreUnknownKeys
@Serializable
@Parcelize
data class FilmSearch(
    val adult: Boolean,
    val backdrop_path: String?,
    val genre_ids: Array<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Float,
    val poster_path: String?,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Float,
    val vote_count: Int
) : Parcelable

@Parcelize
data class ListFilm(
    val list: MutableList<FilmSearch>
) : Parcelable