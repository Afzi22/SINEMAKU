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
}
