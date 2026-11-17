package service

import model.Film
import model.Tayangan
import java.io.File

class CsvPersistence(private val filePath: String = "katalog_film.csv") {

    // Menyimpan data daftar tayangan ke file CSV
    fun simpanKeCsv(daftarTayangan: List<Tayangan>) {
        val file = File(filePath)
        val sb = StringBuilder()

        // Header CSV
        sb.append("id,judul,tahunRilis,genre,durasiMenit,sutradara,kategori\n")

        for (t in daftarTayangan) {
            val genreString = t.genre.joinToString(";") // Menggabungkan list genre dengan pemisah titik koma
            val durasi = if (t is Film) t.durasiMenit else 0
            val sutradara = if (t is Film) t.sutradara else "-"

            sb.append("${t.id},")
                .append("\"${t.judul}\",")
                .append("${t.tahunRilis},")
                .append("\"$genreString\",")
                .append("$durasi,")
                .append("\"$sutradara\",")
                .append("${t.kategori()}\n")
        }

        file.writeText(sb.toString())
    }

    // Memuat kembali data dari file CSV ke dalam sistem
    fun muatDariCsv(): List<Tayangan> {
        val file = File(filePath)
        if (!file.exists()) return emptyList()

        val hasil = mutableListOf<Tayangan>()
        val barisList = file.readLines()

        // Lewati baris pertama (header) jika file tidak kosong
        for (i in 1 until barisList.size) {
            val baris = barisList[i]
            // Parser sederhana untuk memisahkan kolom CSV
            val kolom = baris.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)".toRegex())

            if (kolom.size >= 7) {
                val id = kolom[0].trim()
                val judul = kolom[1].trim().removeSurrounding("\"")
                val tahunRilis = kolom[2].trim().toIntOrNull() ?: 2024
                val genre = kolom[3].trim().removeSurrounding("\"").split(";")
                val durasiMenit = kolom[4].trim().toIntOrNull() ?: 120
                val sutradara = kolom[5].trim().removeSurrounding("\"")
                val kategori = kolom[6].trim()

                if (kategori == "FILM") {
                    hasil.add(Film(id, judul, tahunRilis, genre, durasiMenit, sutradara))
                }
                // Jika nanti ada Serial atau Dokumenter, bisa ditambahkan percabangan di sini
            }
        }

        return hasil
    }
}