
package com.gestao.alugueis.contrato.dto

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class ContratoResponse(

    val id: Long?,

    val imovelId: Long,
    val imovelTitulo: String,

    val inquilinoId: Long,
    val inquilinoNome: String,
    val inquilinoCpf: String,

    val finalidade: String,
    val valorAluguel: BigDecimal,
    val diaVencimento: Int,

    val dataInicio: LocalDate,
    val dataFim: LocalDate?,

    val indiceReajuste: String?,
    val valorCaucao: BigDecimal?,

    val observacoes: String?,

    val locadorNome: String?,
    val locadorDocumento: String?,
    val locadorEndereco: String?,

    val status: String,

    val criadoEm: LocalDateTime?,
    val atualizadoEm: LocalDateTime?,

    val clausulas: List<ContratoClausulaResponse> = emptyList()
)