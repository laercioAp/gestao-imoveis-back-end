
package com.gestao.alugueis.contrato.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size

data class ContratoClausulaRequest(

    @field:NotNull
    @field:Positive
    val ordem: Int,

    @field:NotBlank(message = "O título da cláusula é obrigatório.")
    @field:Size(max = 200)
    val titulo: String,

    @field:NotBlank(message = "O conteúdo da cláusula é obrigatório.")
    val conteudo: String,

    val ativa: Boolean = true,

    val personalizada: Boolean = false
)