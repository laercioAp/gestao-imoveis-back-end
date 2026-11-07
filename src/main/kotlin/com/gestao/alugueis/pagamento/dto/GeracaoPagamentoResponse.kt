
package com.gestao.alugueis.pagamento.dto

import java.time.LocalDate

data class GeracaoPagamentoResponse(
    val competencia: LocalDate,
    val contratosAtivosEncontrados: Int,
    val pagamentosCriados: Int,
    val pagamentosJaExistentes: Int
)