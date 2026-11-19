package com.example.ohmymovies

import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class FilmActivity : AppCompatActivity() {
    lateinit var genres: ChipGroup

    fun addChip(text: String) {
        val chip = Chip(this)
        chip.text = text
        genres.addView(chip)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_film)

        // Initialisation des variables
        val image = findViewById<ImageView>(R.id.image)
        val titre = findViewById<TextView>(R.id.titre)

        genres = findViewById(R.id.genres)
        val synopsis = findViewById<TextView>(R.id.synopsis)
        val dateSortie = findViewById<TextView>(R.id.dateSortie)
        val langue = findViewById<TextView>(R.id.langueFilm)
        val restrictionAge = findViewById<TextView>(R.id.restrictionAge)
        val score = findViewById<RatingBar>(R.id.score)
        val votePar = findViewById<TextView>(R.id.nbr_avis)
        val data = intent.getIntExtra("idFilm", -1)

        lifecycleScope.launch {
            val getFilm = DAO.getById(data)
            getFilm?.let {
                genres.removeAllViews()
                Picasso.get().load("https://image.tmdb.org/t/p/w1280" + it.backdrop_path)
                    .into(image)
                titre.text = it.title
                it.genres.forEach { genre ->
                    addChip(genre.name)
                }
                if (it.overview.isNotEmpty()) {
                    synopsis.text = it.overview
                }
                dateSortie.text = it.release_date
                langue.text = it.original_language.uppercase()
                if (it.adult) restrictionAge.text =
                    getString(R.string.oui) else getString(R.string.non)
                score.progress = it.vote_average.toInt()
                votePar.text = it.vote_count.toString()
            }
        }

        val btnRetour = findViewById<ImageButton>(R.id.btn_retour)
        btnRetour.setOnClickListener {
            finish()
        }
    }
}