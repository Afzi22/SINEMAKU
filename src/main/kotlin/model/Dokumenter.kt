package model

class Dokumenter(
    id: String,
    judul: String,
    tahunRilis: Int,
    genre: List<String>,
    val durasiMenit: Int,
    val topik: String,
    val narator: String
) : Tayangan(id, judul, tahunRilis, genre) {
    override fun kategori(): String = "DOKUMENTER"

    override fun totalMenit(): Int = durasiMenit

    override fun deskripsi(): String =
        super.deskripsi() + " | Topik: $topik | Narator: $narator"
}
