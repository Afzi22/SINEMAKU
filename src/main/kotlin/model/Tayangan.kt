package model

/**
 * Konsep umum dari semua tontonan di katalog.
 * Tidak boleh dibuat objeknya langsung, harus lewat Film, Serial, atau Dokumenter.
 *
 * Atribut bisa dibaca dari luar, tetapi hanya bisa diubah lewat perbaruiInformasi().
 */
abstract class Tayangan(
    val id: String,
    judul: String,
    tahunRilis: Int,
    genre: List<String>
) {
    var judul: String = judul
        private set

    var tahunRilis: Int = tahunRilis
        private set

    // salinan, supaya list asli dari luar tidak bisa mengubah isi genre di sini
    var genre: List<String> = genre.toList()
        private set

    init {
        require(judul.isNotBlank()) { "Judul tidak boleh kosong." }
        require(tahunRilis in 1888..2100) { "Tahun rilis tidak valid: $tahunRilis." }
        require(genre.isNotEmpty()) { "Tayangan harus punya minimal satu genre." }
    }

    /**
     * Memperbarui judul, tahun rilis, dan genre sekaligus.
     * Semua nilai dicek dulu. Jika ada yang tidak valid, tidak ada yang berubah.
     */
    fun perbaruiInformasi(judulBaru: String, tahunBaru: Int, genreBaru: List<String>) {
        require(judulBaru.isNotBlank()) { "Judul tidak boleh kosong." }
        require(tahunBaru in 1888..2100) { "Tahun rilis tidak valid: $tahunBaru." }
        require(genreBaru.isNotEmpty()) { "Tayangan harus punya minimal satu genre." }

        judul = judulBaru
        tahunRilis = tahunBaru
        genre = genreBaru.toList()
    }

    /** Jenis tayangan, diisi oleh tiap subclass. */
    abstract fun kategori(): String

    /** Total durasi dalam menit. Cara menghitungnya beda di tiap jenis. */
    abstract fun totalMenit(): Int

    open fun deskripsi(): String =
        "[${kategori()}] $judul ($tahunRilis) | ${genre.joinToString(", ")} | ${totalMenit()} menit"

    override fun toString(): String = deskripsi()
}