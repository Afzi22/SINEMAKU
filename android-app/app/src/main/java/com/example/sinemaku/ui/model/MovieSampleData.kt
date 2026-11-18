
package com.example.sinemaku.ui.model

import model.Film

object MovieSampleData {

    val movies: List<MovieUiModel> = listOf(

        MovieUiModel(
            film = Film(
                id = "FILM-001",
                judul = "Interstellar",
                tahunRilis = 2014,
                genre = listOf("Sci-Fi", "Adventure"),
                durasiMenit = 169,
                sutradara = "Christopher Nolan"
            ),
            rating = 8.7,
            posterName = "poster_interstellar",
            synopsis = "Sekelompok penjelajah melakukan perjalanan luar angkasa untuk mencari planet yang dapat menjadi tempat tinggal baru bagi manusia."
        ),

        MovieUiModel(
            film = Film(
                id = "FILM-002",
                judul = "Inception",
                tahunRilis = 2010,
                genre = listOf("Sci-Fi", "Thriller"),
                durasiMenit = 148,
                sutradara = "Christopher Nolan"
            ),
            rating = 8.8,
            posterName = "poster_inception",
            synopsis = "Seorang pencuri profesional memasuki mimpi orang lain untuk mencuri informasi dan menjalankan misi yang sangat berbahaya."
        ),

        MovieUiModel(
            film = Film(
                id = "FILM-003",
                judul = "The Dark Knight",
                tahunRilis = 2008,
                genre = listOf("Action", "Crime"),
                durasiMenit = 152,
                sutradara = "Christopher Nolan"
            ),
            rating = 9.0,
            posterName = "poster_dark_knight",
            synopsis = "Batman menghadapi Joker, seorang penjahat yang menciptakan kekacauan di Gotham City."
        ),

        MovieUiModel(
            film = Film(
                id = "FILM-004",
                judul = "Dune",
                tahunRilis = 2021,
                genre = listOf("Sci-Fi", "Adventure"),
                durasiMenit = 155,
                sutradara = "Denis Villeneuve"
            ),
            rating = 8.0,
            posterName = "poster_dune",
            synopsis = "Paul Atreides menghadapi konflik politik dan perebutan kekuasaan di planet gurun Arrakis."
        ),

        MovieUiModel(
            film = Film(
                id = "FILM-005",
                judul = "The Chronicles of Narnia: Prince Caspian",
                tahunRilis = 2008,
                genre = listOf("Fantasy", "Adventure"),
                durasiMenit = 150,
                sutradara = "Andrew Adamson"
            ),
            rating = 6.5,
            posterName = "poster_narnia_prince_caspian",
            synopsis = "Empat bersaudara Pevensie kembali ke Narnia untuk membantu Pangeran Caspian merebut kembali kerajaannya."
        )
    )
}
