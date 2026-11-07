
package com.gestao.alugueis.contrato.dto

data class ContratoClausulaResponse(

    val id: Long?,

    val ordem: Int,

    val titulo: String,

    val conteudo: String,

    val ativa: Boolean,

    val personalizada: Boolean
)