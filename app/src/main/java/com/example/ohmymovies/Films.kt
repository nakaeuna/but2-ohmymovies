package com.example.ohmymovies

class Films {

    companion object {

        val COMPARATOR_DEFAULT =
            Comparator<FilmGetById> { t0, t1 -> t0.id.compareTo(t1.id) }
        val COMPARATOR_ALPHABETICAL =
            Comparator<FilmGetById> { t0, t1 -> t0.title.compareTo(t1.title) }
        val COMPARATOR_POPULAIRE =
            Comparator<FilmGetById> { t0, t1 -> t1.popularity.compareTo(t0.popularity) }
        val COMPARATOR_RECENT =
            Comparator<FilmGetById> { t0, t1 -> t1.release_date.compareTo(t0.release_date) }
        val COMPARATOR_ANCIEN =
            Comparator<FilmGetById> { t0, t1 -> t0.release_date.compareTo(t1.release_date) }

    }
}


