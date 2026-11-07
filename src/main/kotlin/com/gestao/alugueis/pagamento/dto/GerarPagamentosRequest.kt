
package com.gestao.alugueis.pagamento.dto

import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class GerarPagamentosRequest(

    @field:NotNull(message = "A competência é obrigatória.")
    val competencia: LocalDate,

    val contratoId: Long? = null

)