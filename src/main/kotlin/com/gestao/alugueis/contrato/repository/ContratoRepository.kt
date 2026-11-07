
package com.gestao.alugueis.contrato.repository

import com.gestao.alugueis.contrato.entity.Contrato
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ContratoRepository : JpaRepository<Contrato, Long> {

    fun findByStatus(status: String): List<Contrato>

    fun findByImovelId(imovelId: Long): List<Contrato>

    fun findByInquilinoId(inquilinoId: Long): List<Contrato>

    fun findByStatusAndImovelId(
        status: String,
        imovelId: Long
    ): List<Contrato>

    fun existsByImovelIdAndStatus(
        imovelId: Long,
        status: String
    ): Boolean
}