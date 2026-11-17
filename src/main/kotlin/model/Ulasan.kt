package model

import java.time.LocalDate
import java.util.UUID

/**
 * Satu catatan penilaian dari seorang pengguna untuk satu tayangan.
 * Menyimpan id (bukan objeknya) supaya mudah disimpan ke file/database.
 */
data class Ulasan(
    val id: String = UUID.randomUUID().toString().take(8),
    val tayanganId: String,
    val penggunaId: String,
    val nilai: Int,
    val komentar: String = "",
    val tanggal: LocalDate = LocalDate.now()
) {
    init {
        require(nilai in 1..10) { "Nilai ulasan harus antara 1 sampai 10, bukan $nilai." }
    }
}
