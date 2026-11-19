package com.example.ohmymovies

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso

class FilmAdapter(context: Context, films: MutableList<FilmGetById>) : ArrayAdapter<FilmGetById>(
    context, 0, films
) {
    class ViewHolder {
        lateinit var titreFilm: TextView
        lateinit var informationsFilm: TextView
        lateinit var posterFilm: ImageView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var row = convertView
        val vh: ViewHolder

        if (row == null) {
            row = LayoutInflater.from(parent.context).inflate(R.layout.film_item, parent, false)
            vh = ViewHolder()
            vh.titreFilm = row.findViewById(R.id.titreFilm)
            vh.informationsFilm = row.findViewById(R.id.informationsFilm)
            vh.posterFilm = row.findViewById(R.id.posterFilm)
            row.tag = vh
        } else {
            vh = row.tag as ViewHolder
        }

        val film = getItem(position)
        if (film != null) {
            vh.titreFilm.text = film.title
            var informations = film.release_date
            if (film.tagline.isNotEmpty()) {
                informations += " - " + film.tagline
            }
            vh.informationsFilm.text = informations
            Picasso.get().load("https://image.tmdb.org/t/p/w500" + film.backdrop_path)
                .into(vh.posterFilm)
        }

        return row as View
    }
}