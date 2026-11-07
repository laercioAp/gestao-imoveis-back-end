
package com.gestao.alugueis.inquilino.dto

import java.time.LocalDateTime

data class InquilinoResponse(

    val id: Long?,
    val nome: String,
    val cpf: String,
    val email: String?,
    val telefone: String,
    val endereco: String?,
    val numero: String?,
    val complemento: String?,
    val bairro: String?,
    val cidade: String?,
    val estado: String?,
    val cep: String?,
    val ativo: Boolean,
    val criadoEm: LocalDateTime?,
    val atualizadoEm: LocalDateTime?
)