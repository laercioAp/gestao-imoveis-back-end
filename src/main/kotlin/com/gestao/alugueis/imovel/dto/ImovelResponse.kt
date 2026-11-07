
package com.gestao.alugueis.imovel.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class ImovelResponse(
    val id: Long?,
    val titulo: String,
    val tipo: String,
    val descricao: String?,
    val endereco: String,
    val numero: String?,
    val complemento: String?,
    val bairro: String?,
    val cidade: String,
    val estado: String,
    val cep: String?,
    val valorAluguel: BigDecimal,
    val ativo: Boolean,
    val criadoEm: LocalDateTime?,
    val atualizadoEm: LocalDateTime?
)