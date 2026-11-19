package com.example.ohmymovies

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.launch
import java.util.Calendar

class RechercheActivity : AppCompatActivity() {

    companion object {
        const val ANNEE_MIN = 1874 // Plus petite année de film trouvable dans TMDB
        val ANNEE_ACTUELLE = Calendar.getInstance().get(Calendar.YEAR)
        val genresChip = mutableListOf<String>()
    }

    // Initialisation des éléments de la page Recherche
    lateinit var et_titreFilm: EditText
    lateinit var s_choixGenre: Spinner
    lateinit var np_choixAnneeSortie: NumberPicker
    lateinit var bt_populaire: Switch
    lateinit var bt_tousPublics: Switch
    lateinit var bt_recherche: Button
    lateinit var bt_filmsPopulaires: Button
    lateinit var cg_genres: ChipGroup

    fun addChipGenre(text: String) {
        val chip = Chip(this)
        chip.text = text
        chip.isCloseIconVisible = true
        chip.setOnClickListener {
            try {
                genresChip.remove(text)
                cg_genres.removeView(chip)
                if (genresChip.isEmpty()) {
                    s_choixGenre.setSelection(0, true)
                }
            } catch (e: Error) {
                Log.e(e.toString(), "Une erreur est survenue lors de la suppression du chip $text")
            }
        }
        cg_genres.addView(chip)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recherche)

        // Initialisation des variables
        et_titreFilm = findViewById(R.id.et_titreFilm)
        s_choixGenre = findViewById(R.id.s_choixGenre)
        np_choixAnneeSortie = findViewById(R.id.np_choixAnneeSortie)
        bt_populaire = findViewById(R.id.bt_populaire)
        bt_tousPublics = findViewById(R.id.bt_tousPublics)
        bt_recherche = findViewById(R.id.bt_recherche)
        bt_filmsPopulaires = findViewById(R.id.bt_filmsPopulaires)
        cg_genres = findViewById(R.id.genresRecherche)

        // Dropdown Menu pour la sélection du genre
        var genresArray = mutableListOf<Genre>()
        val genresStringArray = mutableListOf<String>()

        val adapter = object : ArrayAdapter<String>(
            this@RechercheActivity,
            android.R.layout.simple_spinner_item,
            genresStringArray
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent) as TextView
                if (position == 0) {
                    view.setTextColor(getColor(R.color.gris))
                }
                return view
            }

            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                if (position == 0) {
                    view.setTextColor(getColor(R.color.gris))
                }
                return view
            }
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        s_choixGenre.adapter = adapter
        s_choixGenre.setSelection(0, false)

        lifecycleScope.launch {
            val reponseGenre = DAO.get_genre()
            reponseGenre?.let {
                genresArray = it.genres.toMutableList()
                genresStringArray.add(getString(R.string.s_lectionner_un_ou_plusieurs_genres))
                genresArray.forEach { genre ->
                    genresStringArray.add(genre.name)
                }
                adapter.notifyDataSetChanged()
            }
        }

        s_choixGenre.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    (view as? TextView)?.setTextColor(getColor(R.color.gris))
                    return
                }

                if (genresStringArray[position] !in genresChip) {
                    addChipGenre(genresStringArray[position])
                    genresChip.add(genresStringArray[position])
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // NumberPicker pour la sélection de l'année de sortie du film
        np_choixAnneeSortie.minValue = ANNEE_MIN
        np_choixAnneeSortie.maxValue = ANNEE_ACTUELLE + 1
        np_choixAnneeSortie.wrapSelectorWheel = false
        val annees = mutableListOf<String>()
        for (i in ANNEE_MIN..ANNEE_ACTUELLE) {
            annees.add(i.toString())
        }
        annees.add(getString(R.string.notSpecified))
        np_choixAnneeSortie.displayedValues = annees.toTypedArray()
        np_choixAnneeSortie.value = np_choixAnneeSortie.maxValue

        bt_recherche.setOnClickListener {
            // Envoyer la requête à l'API
            val filmsARenvoyer = mutableListOf<FilmSearch>()

            if (et_titreFilm.text.toString().trim().isNotEmpty()) {
                lifecycleScope.launch {
                    // Appel API pour rechercher des films en fonction des paramètres du titre
                    val titre = et_titreFilm.text.toString()
                    val reponse = DAO.search(
                        titre,
                        adult = bt_tousPublics.isActivated,
                        popular = bt_populaire.isActivated
                    )

                    reponse?.let {
                        val films = it.results

                        films.forEach { film ->
                            Log.i("Recherche Titre", film.title)
                            filmsARenvoyer.add(film)
                        }
                    }
                    val i = Intent(this@RechercheActivity, ResultatsActivity::class.java)
                    i.putExtra("films", ListFilm(filmsARenvoyer))
                    startActivity(i)
                }
            } else {
                lifecycleScope.launch {
                    // Appel API pour rechercher des films en fonction des paramètres de recherche
                    val genreIds = mutableListOf<Int>()
                    genresArray.forEach {
                        if (it.name in genresChip) {
                            genreIds.add(it.id)
                            Log.i("genre", it.name)
                        }
                    }
                    val reponse = DAO.discover(
                        annee =
                        if (np_choixAnneeSortie.value == np_choixAnneeSortie.maxValue) null
                        else np_choixAnneeSortie.value,
                        adult = bt_tousPublics.isActivated,
                        genres = genreIds,
                        popular = bt_populaire.isActivated
                    )

                    reponse?.let {
                        val films = it.results

                        films.forEach { film ->
                            Log.i("Recherche filtre", film.title)
                            filmsARenvoyer.add(film)
                        }
                    }

                    // INTENT
                    val i = Intent(this@RechercheActivity, ResultatsActivity::class.java)
                    i.putExtra("films", ListFilm(filmsARenvoyer))
                    startActivity(i)
                }
            }
        }

        bt_filmsPopulaires.setOnClickListener {
            // Affichage des films populaires
            val filmsPopulaires = mutableListOf<FilmSearch>()

            lifecycleScope.launch {
                val reponse = DAO.getPopulars()
                reponse?.let {
                    val films = it.results

                    films.forEach { film ->
                        Log.i("Populaires", film.title)
                        filmsPopulaires.add(film)
                    }
                }

                // INTENT
                val i = Intent(this@RechercheActivity, ResultatsActivity::class.java)
                i.putExtra("films", ListFilm(filmsPopulaires))
                startActivity(i)
            }
        }
    }
}