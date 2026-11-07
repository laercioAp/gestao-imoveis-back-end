
package com.gestao.alugueis.pagamento.dto

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.time.LocalDate

data class PagamentoRequest(

    @field:NotNull(message = "O contrato é obrigatório.")
    val contratoId: Long,

    @field:NotNull(message = "A competência é obrigatória.")
    val competencia: LocalDate,

    @field:NotNull(message = "O valor previsto é obrigatório.")
    @field:DecimalMin(
        value = "0.01",
        message = "O valor previsto deve ser maior que zero."
    )
    val valorPrevisto: BigDecimal,

    val dataVencimento: LocalDate,

    @field:DecimalMin(
        value = "0.00",
        message = "O valor pago não pode ser negativo."
    )
    val valorPago: BigDecimal? = null,

    val dataPagamento: LocalDate? = null,

    @field:Size(max = 30)
    val formaPagamento: String? = null,

    val status: String = "PENDENTE",

    val observacoes: String? = null
)