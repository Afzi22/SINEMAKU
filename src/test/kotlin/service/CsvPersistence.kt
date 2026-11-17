package service

import model.Film
import java.io.File

class CsvPersistence(private val filePath: String = "katalog_film.csv") {

    // Fungsi untuk menyimpan 10 data film ke file CSV
    fun simpanKeFile(films: List<Film>) {
        val file = File(filePath)
        val sb = StringBuilder()

        // Header CSV
        sb.append("id,judul,tahun,genre,rating\n")

        for (film in films) {
            sb.append("${film.id},${film.judul},${film.tahun},${film.genre},${film.rating}\n")
        }

        file.writeText(sb.toString())
        println("Berhasil menyimpan data ke file: $filePath")
    }

    // Fungsi untuk memuat data film dari file CSV
    fun muatDariFile(): List<Film> {
        val file = File(filePath)
        val films = mutableListOf<Film>()

        if (!file.exists()) {
            return films
        }

        val lines = file.readLines()
        for (i in 1 until lines.size) {
            val line = lines[i]
            val parts = line.split(",")
            if (parts.size == 5) {
                val film = Film(
                    id = parts[0],
                    judul = parts[1],
                    tahun = parts[2].toInt(),
                    genre = parts[3],
                    rating = parts[4].toDouble()
                )
                films.add(film)
            }
        }
        println("Berhasil memuat ${films.size} data film dari file: $filePath")
        return films
    }
}