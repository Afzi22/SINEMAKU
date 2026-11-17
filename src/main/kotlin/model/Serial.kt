package model

class Serial(
    id: String,
    judul: String,
    tahunRilis: Int,
    genre: List<String>,
    val jumlahMusim: Int,
    val jumlahEpisode: Int,
    val menitPerEpisode: Int
) : Tayangan(id, judul, tahunRilis, genre) {
    override fun kategori(): String = "SERIAL"

    // beda dari Film: durasi total dihitung dari jumlah episode
    override fun totalMenit(): Int = jumlahEpisode * menitPerEpisode

    override fun deskripsi(): String =
        super.deskripsi() + " | $jumlahMusim musim, $jumlahEpisode episode"
}
