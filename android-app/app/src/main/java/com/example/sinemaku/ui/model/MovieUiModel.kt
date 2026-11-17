
package com.example.sinemaku.ui.model

import model.Film

/**
 * Model khusus tampilan Android SINEMAKU.
 *
 * Data utama film berasal dari model domain
 * yang dibuat oleh Fawwaz.
 *
 * Rating, poster, dan sinopsis merupakan
 * informasi tambahan untuk kebutuhan UI.
 */
data class MovieUiModel(
    val film: Film,
    val rating: Double,
    val posterName: String,
    val synopsis: String
) {

    // ID resmi dari model domain.
    val id: String
        get() = film.id

    // Judul film.
    val title: String
        get() = film.judul

    // Tahun rilis.
    val year: Int
        get() = film.tahunRilis

    // Daftar genre.
    val genres: List<String>
        get() = film.genre

    // Genre dalam format teks.
    val genreText: String
        get() = film.genre.joinToString(", ")

    // Durasi film.
    val durationMinutes: Int
        get() = film.durasiMenit

    // Nama sutradara.
    val director: String
        get() = film.sutradara

    // Format durasi: contoh 2 jam 49 menit.
    val durationText: String
        get() {
            val hours = durationMinutes / 60
            val minutes = durationMinutes % 60

            return if (hours > 0) {
                "$hours jam $minutes menit"
            } else {
                "$minutes menit"
            }
        }

    // Rating yang siap ditampilkan pada UI.
    val ratingText: String
        get() = "$rating / 10"
}
