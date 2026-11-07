
package com.gestao.alugueis.dashboard.dto

import java.math.BigDecimal

data class DashboardResponse(

    val competencia: String,

    val mesAtual: String,

    val recebidos: BigDecimal,

    val pendentes: BigDecimal,

    val atrasados: BigDecimal,

    val alugueis: List<DashboardAluguelResponse>

)

data class DashboardAluguelResponse(

    val id: Long,

    val imovel: String,

    val competencia: String,

    val valor: BigDecimal,

    val vencimento: String,

    val status: String,

    val situacaoVencimento: String

)