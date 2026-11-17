package model

class Film(
    id: String,
    judul: String,
    tahunRilis: Int,
    genre: List<String>,
    val durasiMenit: Int,
    val sutradara: String
) : Tayangan(id, judul, tahunRilis, genre) {
    override fun kategori(): String = "FILM"

    override fun totalMenit(): Int = durasiMenit

    override fun deskripsi(): String = super.deskripsi() + " | Sutradara: $sutradara"
}
