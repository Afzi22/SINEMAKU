package service

import exception.*
import model.Film
import kotlin.test.*

class SinemakuTest {

    private lateinit var catalogService: CineCatalogService

    @BeforeTest
    fun siapkanData() {
        catalogService = CineCatalogService()

        // Memasukkan 10 data film pilihanmu menggunakan objek Film
        catalogService.tambahFilm(Film("F001", "Jangan Buang Ibu", 2024, "Drama", 4.7))
        catalogService.tambahFilm(Film("F002", "Dance of the Damned", 2023, "Horror", 4.3))
        catalogService.tambahFilm(Film("F003", "Gadis Kretek", 2023, "Drama", 4.9))
        catalogService.tambahFilm(Film("F004", "Abadi Nan Jaya", 2024, "Action", 4.5))
        catalogService.tambahFilm(Film("F005", "Sore : Istri dan Masa Depan", 2024, "Romance", 4.8))
        catalogService.tambahFilm(Film("F006", "Sekawan Limo", 2024, "Comedy", 4.6))
        catalogService.tambahFilm(Film("F007", "Spiderman : Brand New Day", 2026, "Action", 4.8))
        catalogService.tambahFilm(Film("F008", "Thor : Love and Thunder", 2022, "Action", 4.2))
        catalogService.tambahFilm(Film("F009", "Ant-Man", 2023, "Sci-Fi", 4.4))
        catalogService.tambahFilm(Film("F010", "Boyfriend on Demand", 2025, "Romance", 4.5))
    }

    @Test
    fun `menambah film dengan ID yang sama harus gagal`() {
        assertFailsWith<DuplikasiIdException> {
            catalogService.tambahFilm(Film("F001", "Film Lain", 2026, "Action", 4.0))
        }
    }

    @Test
    fun `menambahkan film baru berhasil dan masuk ke katalog`() {
        catalogService.tambahFilm(Film("F011", "Avengers Secret Wars", 2027, "Action", 5.0))
        assertEquals(11, catalogService.semuaFilm().size)
    }

    @Test
    fun `pencarian film tidak membedakan huruf besar dan kecil`() {
        assertEquals(1, catalogService.cariFilm("gadis kretek").size)
        assertEquals(0, catalogService.cariFilm("avatar").size)
    }

    @Test
    fun `menambahkan film ke watchlist harus berhasil`() {
        catalogService.tambahKeWatchlist("F003") // Gadis Kretek
        assertEquals(1, catalogService.lihatWatchlist().size)
        assertEquals("F003", catalogService.lihatWatchlist().first().id)
    }

    @Test
    fun `memberikan rating di luar rentang yang valid harus ditolak`() {
        assertFailsWith<InputTidakValidException> {
            catalogService.beriRating("F001", 6.0)
        }
    }
}