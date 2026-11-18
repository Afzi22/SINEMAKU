
package exception

sealed class KatalogException(
    message: String
) : Exception(message)

class DuplikasiIdException(id: String) :
    KatalogException("ID tayangan '$id' sudah terdaftar.")

class TayanganTidakDitemukanException(id: String) :
    KatalogException("Tayangan dengan ID '$id' tidak ditemukan.")

class InputTidakValidException(message: String) :
    KatalogException(message)
