
package service

import model.Tayangan
import exception.DuplikasiIdException
import exception.TayanganTidakDitemukanException
import exception.InputTidakValidException

class KatalogService : Pencarian {

    private val katalog: MutableMap<String, Tayangan> = mutableMapOf()

    // Menambahkan tayangan baru
    fun tambahTayangan(tayangan: Tayangan) {
        validasiTayangan(tayangan)

        if (katalog.containsKey(tayangan.id)) {
            throw DuplikasiIdException(tayangan.id)
        }

        katalog[tayangan.id] = tayangan
    }

    // Menampilkan seluruh tayangan
    fun semuaTayangan(): List<Tayangan> {
        return katalog.values.toList()
    }

    // Mencari tayangan berdasarkan ID
    fun cariBerdasarkanId(id: String): Tayangan? {
        if (id.isBlank()) {
            throw InputTidakValidException(
                "ID tidak boleh kosong."
            )
        }

        return katalog[id]
    }

    // Mengubah informasi tayangan
    fun ubahTayangan(
        id: String,
        judulBaru: String,
        tahunBaru: Int,
        genreBaru: List<String>
    ) {
        if (id.isBlank()) {
            throw InputTidakValidException(
                "ID tidak boleh kosong."
            )
        }

        val tayangan = katalog[id]
            ?: throw TayanganTidakDitemukanException(id)

        validasiInformasi(judulBaru, tahunBaru, genreBaru)

        tayangan.perbaruiInformasi(
            judulBaru,
            tahunBaru,
            genreBaru
        )
    }

    // Menghapus tayangan
    fun hapusTayangan(id: String) {
        if (id.isBlank()) {
            throw InputTidakValidException(
                "ID tidak boleh kosong."
            )
        }

        if (katalog.remove(id) == null) {
            throw TayanganTidakDitemukanException(id)
        }
    }

    // Mencari tayangan berdasarkan judul atau genre
    override fun cariTayangan(keyword: String): List<Tayangan> {
        if (keyword.isBlank()) {
            throw InputTidakValidException(
                "Kata kunci pencarian tidak boleh kosong."
            )
        }

        val kataKunci = keyword.trim()

        return katalog.values.filter { tayangan ->
            tayangan.judul.contains(
                kataKunci,
                ignoreCase = true
            ) ||
                    tayangan.genre.any {
                        it.contains(kataKunci, ignoreCase = true)
                    }
        }
    }

    // Memfilter tayangan berdasarkan jenis
    override fun filterJenis(jenis: String): List<Tayangan> {
        if (jenis.isBlank()) {
            throw InputTidakValidException(
                "Jenis tayangan tidak boleh kosong."
            )
        }

        val jenisValid = jenis.trim().uppercase()

        if (jenisValid !in listOf("FILM", "SERIAL", "DOKUMENTER")) {
            throw InputTidakValidException(
                "Jenis tayangan tidak dikenali."
            )
        }

        return katalog.values.filter {
            it.kategori().equals(jenisValid, ignoreCase = true)
        }
    }

    // Memfilter tayangan berdasarkan genre
    override fun filterGenre(genre: String): List<Tayangan> {
        if (genre.isBlank()) {
            throw InputTidakValidException(
                "Genre tidak boleh kosong."
            )
        }

        return katalog.values.filter { tayangan ->
            tayangan.genre.any {
                it.equals(genre.trim(), ignoreCase = true)
            }
        }
    }

    // Memvalidasi data tayangan
    private fun validasiTayangan(tayangan: Tayangan) {
        if (tayangan.id.isBlank()) {
            throw InputTidakValidException(
                "ID tayangan tidak boleh kosong."
            )
        }

        validasiInformasi(
            tayangan.judul,
            tayangan.tahunRilis,
            tayangan.genre
        )
    }

    // Memvalidasi informasi tayangan
    private fun validasiInformasi(
        judul: String,
        tahunRilis: Int,
        genre: List<String>
    ) {
        if (judul.isBlank()) {
            throw InputTidakValidException(
                "Judul tayangan tidak boleh kosong."
            )
        }

        if (tahunRilis !in 1888..2100) {
            throw InputTidakValidException(
                "Tahun rilis harus antara 1888 sampai 2100."
            )
        }

        if (genre.isEmpty()) {
            throw InputTidakValidException(
                "Tayangan harus memiliki minimal satu genre."
            )
        }
    }
}
