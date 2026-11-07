
package com.gestao.alugueis.imovel.repository

import com.gestao.alugueis.imovel.entity.Imovel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ImovelRepository : JpaRepository<Imovel, Long> {

    fun findByAtivo(ativo: Boolean): List<Imovel>

    fun findByTituloContainingIgnoreCase(titulo: String): List<Imovel>
}