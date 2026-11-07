
package com.gestao.alugueis.recibo.repository

import com.gestao.alugueis.recibo.entity.Recibo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReciboRepository : JpaRepository<Recibo, Long> {

    fun findByPagamentoId(pagamentoId: Long): Recibo?

    fun findByNumeroRecibo(numeroRecibo: String): Recibo?

    fun existsByPagamentoId(pagamentoId: Long): Boolean

}