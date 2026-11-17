
package service

import model.Tayangan

interface Pencarian {

    fun cariTayangan(keyword: String): List<Tayangan>

    fun filterJenis(jenis: String): List<Tayangan>

    fun filterGenre(genre: String): List<Tayangan>
}

