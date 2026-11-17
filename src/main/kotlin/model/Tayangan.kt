package model

/**
 * Konsep umum dari semua tontonan di katalog.
 * Tidak boleh dibuat objeknya langsung, harus lewat Film, Serial, atau Dokumenter.
 */
abstract class Tayangan(
    val id: String,
    var judul: String,
    val tahunRilis: Int,
    genre: List<String>
) {
    // salinan, supaya list asli dari luar tidak bisa mengubah isi genre di sini
    val genre: List<String> = genre.toList()

    /** Jenis tayangan, diisi oleh tiap subclass. */
    abstract fun kategori(): String

    /** Total durasi dalam menit. Cara menghitungnya beda di tiap jenis. */
    abstract fun totalMenit(): Int

    open fun deskripsi(): String =
        "[${kategori()}] $judul ($tahunRilis) | ${genre.joinToString(", ")} | ${totalMenit()} menit"

    override fun toString(): String = deskripsi()
}
