
package com.gestao.alugueis.inquilino.repository

import com.gestao.alugueis.inquilino.entity.Inquilino
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InquilinoRepository : JpaRepository<Inquilino, Long> {

    fun findByCpf(cpf: String): Inquilino?

    fun existsByCpf(cpf: String): Boolean

    fun findByAtivo(ativo: Boolean): List<Inquilino>

    fun findByNomeContainingIgnoreCase(nome: String): List<Inquilino>
}