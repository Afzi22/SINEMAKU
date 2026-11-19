package com.example.sinemaku.ui.model

import model.Tayangan
import model.Film
import model.Serial
import model.Dokumenter

/** Adapter UI: satu bentuk tampilan untuk seluruh subclass Tayangan. */
data class TayanganUiModel(
    val tayangan: Tayangan,
    val rating: Double,
    val posterName: String,
    val synopsis: String
) {
<<<<<<< HEAD

    // ID resmi dari model domain.
    val id: String
        get() = film.id

    // Judul film.
    val title: String
        get() = film.judul

    // Tahun rilis.
    val year: Int
        get() = film.tahunRilis

    // Kategori untuk kebutuhan tampilan Android.
    val category: String
        get() = when (film.kategori()) {
            "FILM" -> "Film"
            else -> film.kategori()
        }

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

    // Label informasi pembuat film untuk UI.
    val creatorLabel: String
        get() = "Sutradara: $director"

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
=======
    val id: String get() = tayangan.id
    val title: String get() = tayangan.judul
    val year: Int get() = tayangan.tahunRilis
    val genres: List<String> get() = tayangan.genre
    val genreText: String get() = genres.joinToString(", ")
    val category: String get() = when (tayangan) {
        is Film -> "Film"
        is Serial -> "Serial TV"
        is Dokumenter -> "Dokumenter"
        else -> tayangan.kategori()
    }
    val durationText: String get() {
        val minutes = tayangan.totalMenit()
        val hours = minutes / 60
        val rest = minutes % 60
        return if (hours > 0) "$hours jam $rest menit" else "$rest menit"
    }
    val creatorLabel: String get() = when (val t = tayangan) {
        is Film -> "Sutradara: ${t.sutradara}"
        is Serial -> "${t.jumlahMusim} musim • ${t.jumlahEpisode} episode • ${t.menitPerEpisode} menit/episode"
        is Dokumenter -> "Topik: ${t.topik} • Narator: ${t.narator}"
        else -> ""
    }
>>>>>>> 4ec6675e758aab2dfc0be0a5955fc6f7725089f4
}
