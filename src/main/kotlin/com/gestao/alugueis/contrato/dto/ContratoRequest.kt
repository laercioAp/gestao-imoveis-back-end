
package com.gestao.alugueis.contrato.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.time.LocalDate

data class ContratoRequest(

    @field:NotNull(message = "O imóvel é obrigatório.")
    val imovelId: Long,

    @field:NotNull(message = "O inquilino é obrigatório.")
    val inquilinoId: Long,

    @field:NotBlank(message = "A finalidade do contrato é obrigatória.")
    @field:Size(max = 100)
    val finalidade: String,

    @field:NotNull(message = "O valor do aluguel é obrigatório.")
    @field:DecimalMin(
        value = "0.01",
        message = "O aluguel deve ser maior que zero."
    )
    val valorAluguel: BigDecimal,

    @field:NotNull(message = "O dia de vencimento é obrigatório.")
    @field:Min(value = 1, message = "O vencimento mínimo é dia 1.")
    @field:Max(value = 31, message = "O vencimento máximo é dia 31.")
    val diaVencimento: Int,

    @field:NotNull(message = "A data de início é obrigatória.")
    val dataInicio: LocalDate,

    val dataFim: LocalDate? = null,

    @field:Size(max = 50)
    val indiceReajuste: String? = null,

    @field:DecimalMin(
        value = "0.00",
        message = "O valor da caução não pode ser negativo."
    )
    val valorCaucao: BigDecimal? = null,

    val observacoes: String? = null,

    @field:NotBlank(message = "O nome do locador é obrigatório.")
    @field:Size(max = 200)
    val locadorNome: String,

    @field:Size(max = 30)
    val locadorDocumento: String? = null,

    val locadorEndereco: String? = null,

    val status: String = "RASCUNHO",

    @field:Valid
    val clausulas: List<ContratoClausulaRequest> = emptyList()
)