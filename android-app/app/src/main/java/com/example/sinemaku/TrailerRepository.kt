package com.example.sinemaku

/** Pemetaan judul katalog ke video lokal di res/raw. */
object TrailerRepository {
    private val trailers = mapOf(
        "interstellar" to R.raw.trailer_film_interstellar,
        "inception" to R.raw.trailer_film_inception,
        "dune" to R.raw.trailer_film_dune,
        "the dark knight" to R.raw.trailer_film_dark_knight,
        "dark knight" to R.raw.trailer_film_dark_knight,
        "narnia" to R.raw.trailer_film_narnia,
        "the chronicles of narnia" to R.raw.trailer_film_narnia,
        "the chronicles of narnia: the lion, the witch and the wardrobe" to R.raw.trailer_film_narnia,
        "stranger things" to R.raw.trailer_series_stranger_things,
        "dark" to R.raw.trailer_series_dark,
        "wednesday" to R.raw.trailer_serial_wednesday,
        "my octopus teacher" to R.raw.trailer_dokumenter_my_octopus_teacher,
        "our planet" to R.raw.trailer_dokumenter_our_planet,
        "the social dilemma" to R.raw.trailer_dokumenter_the_social_dilemma
    )

    fun getTrailer(title: String): Int? =
        trailers[title.trim().lowercase().replace(Regex("\\s+"), " ")]
}
