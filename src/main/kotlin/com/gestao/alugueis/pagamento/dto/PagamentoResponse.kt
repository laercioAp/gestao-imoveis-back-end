
package com.gestao.alugueis.pagamento.dto

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class PagamentoResponse(

    val id: Long?,

    val contratoId: Long,

    val imovelId: Long,

    val imovelTitulo: String,

    val inquilinoId: Long,

    val inquilinoNome: String,

    val competencia: LocalDate,

    val valorPrevisto: BigDecimal,

    val valorPago: BigDecimal?,

    val dataVencimento: LocalDate,

    val dataPagamento: LocalDate?,

    val formaPagamento: String?,

    val status: String,

    val situacaoVencimento: String,

    val observacoes: String?,

    val criadoEm: LocalDateTime?,

    val atualizadoEm: LocalDateTime?
)