package com.example.ohmymovies

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResultatsActivity : AppCompatActivity() {

    lateinit var btn_home: ImageButton
    lateinit var listeFilms: ListView
    lateinit var filmAdapter: FilmAdapter
    lateinit var s_trie_par: Spinner
    private val filmList = mutableListOf<FilmGetById>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultats)

        // Initialisation des variables
        btn_home = findViewById(R.id.btn_home)
        listeFilms = findViewById(R.id.listeFilms)
        s_trie_par = findViewById(R.id.s_trie_par)
        filmAdapter = FilmAdapter(this, filmList)
        listeFilms.adapter = filmAdapter

        val films = intent.getParcelableExtra<ListFilm>("films")?.list ?: emptyList()

        lifecycleScope.launch(Dispatchers.IO) {
            val fetchedFilms = films.mapNotNull { film ->
                DAO.getById(film.id)
            }
            withContext(Dispatchers.Main) {
                filmList.addAll(fetchedFilms)
                filmAdapter.notifyDataSetChanged()
            }
        }

        listeFilms.setOnItemClickListener { _, _, pos, _ ->
            val versFilm = Intent(this@ResultatsActivity, FilmActivity::class.java)
            versFilm.putExtra("idFilm", filmList[pos].id)
            startActivity(versFilm)
        }

        val tri = mutableListOf(
            getString(R.string.defaut),
            getString(R.string.alphabet),
            getString(R.string.populaire),
            getString(R.string.recent),
            getString(R.string.ancien))

        val adapterTri = ArrayAdapter<String>(this@ResultatsActivity, android.R.layout.simple_spinner_dropdown_item, tri)
        adapterTri.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        s_trie_par.adapter = adapterTri
        s_trie_par.setSelection(0)

        s_trie_par.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> filmList.sortWith(Films.COMPARATOR_DEFAULT)
                    1 -> filmList.sortWith(Films.COMPARATOR_ALPHABETICAL)
                    2 -> filmList.sortWith(Films.COMPARATOR_POPULAIRE)
                    3 -> filmList.sortWith(Films.COMPARATOR_RECENT)
                    4 -> filmList.sortWith(Films.COMPARATOR_ANCIEN)
                }
                filmAdapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        btn_home.setOnClickListener {
            finish()
        }
    }
}