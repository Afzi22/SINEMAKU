package service

import exception.*
import model.Film
import kotlin.test.*

class SinemakuTest {

    private lateinit var catalogService: KatalogService

    @BeforeTest
    fun siapkanData() {
        catalogService = KatalogService()

        // Memasukkan 10 data film pilihanmu menggunakan method tambahTayangan milik Miftah
        catalogService.tambahTayangan(Film("F001", "Jangan Buang Ibu", 2024, listOf("Drama"), 120, "Sutradara A"))
        catalogService.tambahTayangan(Film("F002", "Dance of the Damned", 2023, listOf("Horror"), 105, "Sutradara B"))
        catalogService.tambahTayangan(Film("F003", "Gadis Kretek", 2023, listOf("Drama"), 110, "Kamila Andini"))
        catalogService.tambahTayangan(Film("F004", "Abadi Nan Jaya", 2024, listOf("Action"), 115, "Sutradara D"))
        catalogService.tambahTayangan(Film("F005", "Sore : Istri dan Masa Depan", 2024, listOf("Romance"), 100, "Yandy Laurens"))
        catalogService.tambahTayangan(Film("F006", "Sekawan Limo", 2024, listOf("Comedy"), 110, "Bayu Skak"))
        catalogService.tambahTayangan(Film("F007", "Spiderman : Brand New Day", 2026, listOf("Action"), 140, "Marvel"))
        catalogService.tambahTayangan(Film("F008", "Thor : Love and Thunder", 2022, listOf("Action"), 119, "Taika Waititi"))
        catalogService.tambahTayangan(Film("F009", "Ant-Man", 2023, listOf("Sci-Fi"), 125, "Peyton Reed"))
        catalogService.tambahTayangan(Film("F010", "Boyfriend on Demand", 2025, listOf("Romance"), 102, "Sutradara J"))
    }

    @Test
    fun `menambah tayangan dengan ID yang sama harus gagal`() {
        assertFailsWith<DuplikasiIdException> {
            catalogService.tambahTayangan(Film("F001", "Film Lain", 2026, listOf("Action"), 90, "Sutradara X"))
        }
    }

    @Test
    fun `menambahkan tayangan baru berhasil dan masuk ke katalog`() {
        catalogService.tambahTayangan(Film("F011", "Avengers Secret Wars", 2027, listOf("Action"), 150, "Russo Brothers"))
        assertEquals(11, catalogService.semuaTayangan().size)
    }

    @Test
    fun `pencarian tayangan berdasarkan keyword tidak membedakan huruf besar dan kecil`() {
        assertEquals(1, catalogService.cariTayangan("gadis kretek").size)
        assertEquals(0, catalogService.cariTayangan("avatar").size)
    }

    @Test
    fun `pencarian berdasarkan ID yang tidak ada mengembalikan null`() {
        assertNull(catalogService.cariBerdasarkanId("F999"))
    }

    @Test
    fun `tahun rilis di luar rentang valid harus ditolak`() {
        assertFailsWith<IllegalArgumentException> {
            catalogService.tambahTayangan(Film("F012", "Film Masa Depan", 2150, listOf("Sci-Fi"), 100, "Sutradara Z"))
        }
    }
}