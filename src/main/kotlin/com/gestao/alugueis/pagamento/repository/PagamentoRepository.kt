
package com.gestao.alugueis.pagamento.repository

import com.gestao.alugueis.pagamento.entity.Pagamento
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface PagamentoRepository : JpaRepository<Pagamento, Long> {

    fun findByContratoId(contratoId: Long): List<Pagamento>

    fun findByStatus(status: String): List<Pagamento>

    fun findByContratoIdAndStatus(
        contratoId: Long,
        status: String
    ): List<Pagamento>

    fun findByCompetencia(competencia: LocalDate): List<Pagamento>

    fun findByDataVencimentoBeforeAndStatus(
        data: LocalDate,
        status: String
    ): List<Pagamento>

    fun existsByContratoIdAndCompetencia(
        contratoId: Long,
        competencia: LocalDate
    ): Boolean

    fun findByContratoIdAndCompetencia(
        contratoId: Long,
        competencia: LocalDate
    ): Pagamento?
}