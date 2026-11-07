
package com.gestao.alugueis.contrato.repository

import com.gestao.alugueis.contrato.entity.ContratoClausula
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ContratoClausulaRepository :
    JpaRepository<ContratoClausula, Long> {

    fun findByContratoIdOrderByOrdemAsc(
        contratoId: Long
    ): List<ContratoClausula>

    fun deleteByContratoId(contratoId: Long)
}
