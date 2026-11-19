package com.example.ohmymovies

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.serializersModule
import kotlinx.serialization.serializer
import java.util.Locale

object DAO {
    private val CLE_API = BuildConfig.TMDB_API_KEY
    private const val URL = "https://api.themoviedb.org/3"
    var lang = when (Locale.getDefault().language) {
        "en" -> "en-US"
        "de" -> "de-DE"
        "it" -> "it-IT"
        "es" -> "es-ES"
        "el" -> "el-GR"
        else -> "fr-FR"
    }

    private fun rechargeLangue() {
        lang = when (Locale.getDefault().language) {
            "en" -> "en-US"
            "de" -> "de-DE"
            "it" -> "it-IT"
            "es" -> "es-ES"
            "el" -> "el-GR"
            else -> "fr-FR"
        }
    }

    private val client = HttpClient(OkHttp) {
        rechargeLangue()
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 15000L
            connectTimeoutMillis = 15000L
            socketTimeoutMillis = 15000L
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.v("Logger Ktor =>", message)
                }
            }
            level = LogLevel.ALL
        }
        install(ResponseObserver) {
            onResponse { response ->
                Log.d("HTTP status:", "${response.status.value}")
            }
        }
    }

    suspend fun getPopulars(page: Int = 1): Reponse_FilmSearch? {
        rechargeLangue()
        val url = "$URL/movie/popular?api_key=$CLE_API&language=$lang&page=$page"
        return fetchData<Reponse_FilmSearch>(url)
    }

    suspend fun getById(id: Int): FilmGetById? {
        rechargeLangue()
        val url = "$URL/movie/$id?api_key=$CLE_API&language=&language=$lang"
        return fetchData<FilmGetById>(url)
    }

    suspend fun search(
        titre: String,
        page: Int = 1,
        adult: Boolean = false,
        popular: Boolean = false
    ): Reponse_FilmSearch? {
        rechargeLangue()
        var url =
            "$URL/search/movie?api_key=$CLE_API&language=$lang&query=$titre&page=$page&include_adult=$adult"
        if (popular) url += "&sort_by=popularity.desc"
        return fetchData<Reponse_FilmSearch>(url)
    }

    suspend fun discover(
        page: Int = 1,
        genres: MutableList<Int>,
        annee: Int?,
        adult: Boolean = false,
        popular: Boolean = false
    ): Reponse_FilmSearch? {
        rechargeLangue()
        var url =
            "$URL/discover/movie?api_key=$CLE_API&include_adult=$adult&language=$lang&page=$page"
        if (annee != null) url += "&year=$annee"
        if (genres.isNotEmpty()) url += "&with_genres=${genres.joinToString(",")}"
        if (popular) url += "&sort_by=popularity.desc"
        return fetchData<Reponse_FilmSearch>(url)
    }

    suspend fun get_genre(): Reponse_Genre? {
        rechargeLangue()
        val url = "https://api.themoviedb.org/3/genre/movie/list?api_key=$CLE_API&language=$lang"
        return fetchData<Reponse_Genre>(url)
    }

    private suspend inline fun <reified T> fetchData(url: String): T? {
        rechargeLangue()
        return try {
            val reponse = client.get(url) {
                contentType(ContentType.Application.Json)
            }
            val jsonBody = reponse.body<String>()
            Log.d("JSON Response", jsonBody) // Log the raw JSON response
            Json.decodeFromString(serializersModule.serializer(), jsonBody)
        } catch (e: Exception) {
            Log.e("DAO Erreur", "Erreur lors de la récupération des données: ${e.message}")
            null
        }
    }
}

@Serializable
data class Reponse_FilmSearch(
    val page: Int,
    val results: List<FilmSearch>,
    val total_pages: Int,
    val total_results: Int
)

@Serializable
data class Reponse_Genre(
    val genres: List<Genre>,
)

