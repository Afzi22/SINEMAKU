package model

/**
 * Kontrak untuk sumber data katalog.
 * Sekarang bisa data lokal, nanti bisa diganti API (TMDB/OMDb) tanpa mengubah service.
 */
interface KatalogSumber {
    fun cariJudul(keyword: String): List<Tayangan>
    fun semuaTayangan(): List<Tayangan>
}
