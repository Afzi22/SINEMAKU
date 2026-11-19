package com.example.sinemaku.ui.model

import model.Film
import model.Serial
import model.Dokumenter

/** Data demo lokal; nantinya diganti sumber data milik tim. */
object TayanganSampleData {
    val items: List<TayanganUiModel> = listOf(
        TayanganUiModel(
            Film("FILM-001", "Interstellar", 2014, listOf("Sci-Fi", "Adventure"), 169, "Christopher Nolan"),
            8.7, "poster_interstellar", "Sekelompok penjelajah melakukan perjalanan antariksa untuk mencari harapan baru bagi manusia."
        ),
        TayanganUiModel(
            Film("FILM-002", "Inception", 2010, listOf("Sci-Fi", "Thriller"), 148, "Christopher Nolan"),
            8.8, "poster_inception", "Seorang ahli infiltrasi mimpi mendapat misi menanamkan ide ke alam bawah sadar."
        ),
        TayanganUiModel(
            Film("FILM-003", "The Dark Knight", 2008, listOf("Action", "Crime"), 152, "Christopher Nolan"),
            9.0, "poster_dark_knight", "Batman menghadapi ancaman Joker yang mengguncang Gotham City."
        ),
        TayanganUiModel(
            Film("FILM-004", "Dune", 2021, listOf("Sci-Fi", "Adventure"), 155, "Denis Villeneuve"),
            8.0, "poster_dune", "Paul Atreides menghadapi takdirnya di planet gurun Arrakis."
        ),
        TayanganUiModel(
            Film("FILM-005", "The Chronicles of Narnia: Prince Caspian", 2008, listOf("Fantasy", "Adventure"), 150, "Andrew Adamson"),
            6.5, "poster_narnia_prince_caspian", "Empat saudara kembali ke Narnia untuk membantu Pangeran Caspian."
        ),
        TayanganUiModel(
            Serial("SER-001", "Stranger Things", 2016, listOf("Sci-Fi", "Mystery"), 4, 34, 50),
            8.6, "poster_stranger_things", "Sekelompok sahabat menyelidiki peristiwa aneh di kota Hawkins."
        ),
        TayanganUiModel(
            Serial("SER-002", "Wednesday", 2022, listOf("Mystery", "Fantasy"), 1, 8, 50),
            8.0, "poster_wednesday", "Wednesday Addams menyelidiki misteri saat bersekolah di Nevermore Academy."
        ),
        TayanganUiModel(
            Serial("SER-003", "Dark", 2017, listOf("Sci-Fi", "Thriller"), 3, 26, 55),
            8.7, "poster_dark_series", "Hilangnya seorang anak membuka misteri lintas generasi di sebuah kota kecil."
        ),
        TayanganUiModel(
            Dokumenter("DOC-001", "Our Planet", 2019, listOf("Nature", "Education"), 50, "Keanekaragaman hayati", "David Attenborough"),
            9.0, "poster_our_planet", "Menjelajahi keindahan alam dan tantangan yang dihadapi satwa liar."
        ),
        TayanganUiModel(
            Dokumenter("DOC-002", "The Social Dilemma", 2020, listOf("Technology", "Social"), 94, "Dampak media sosial", "Berbagai narasumber"),
            7.6, "poster_social_dilemma", "Mengulas pengaruh platform media sosial terhadap perilaku manusia."
        ),
        TayanganUiModel(
            Dokumenter("DOC-003", "My Octopus Teacher", 2020, listOf("Nature", "Education"), 85, "Kehidupan gurita", "Craig Foster"),
            8.1, "poster_my_octopus_teacher", "Hubungan unik seorang penyelam dengan gurita di lautan Afrika Selatan."
        )
    )
}
