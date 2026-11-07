
package com.gestao.alugueis.dashboard.service

import com.gestao.alugueis.dashboard.dto.DashboardAluguelResponse
import com.gestao.alugueis.dashboard.dto.DashboardResponse
import com.gestao.alugueis.pagamento.repository.PagamentoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Service
class DashboardService(
    private val pagamentoRepository: PagamentoRepository
) {

    @Transactional(readOnly = true)
    fun obterResumo(): DashboardResponse {

        val hoje = LocalDate.now()
        val periodo = YearMonth.from(hoje)
        val competencia = periodo.atDay(1)

        val pagamentos = pagamentoRepository.findByCompetencia(competencia)

        val recebidos = pagamentos
            .filter { it.status == "PAGO" }
            .mapNotNull { it.valorPago }
            .fold(BigDecimal.ZERO, BigDecimal::add)

        val pendentes = pagamentos
            .filter { it.status == "PENDENTE" }
            .map { it.valorPrevisto }
            .fold(BigDecimal.ZERO, BigDecimal::add)

        val atrasados = pagamentos
            .filter {
                it.status == "PENDENTE" &&
                        it.dataVencimento.isBefore(hoje)
            }
            .map { it.valorPrevisto }
            .fold(BigDecimal.ZERO, BigDecimal::add)

        val alugueis = pagamentos.map { pagamento ->

            val situacao = when {
                pagamento.status == "PAGO" -> "PAGO"
                pagamento.dataVencimento.isBefore(hoje) -> "ATRASADO"
                else -> "EM_DIA"
            }

            DashboardAluguelResponse(
                id = pagamento.id!!,
                imovel = pagamento.contrato.imovel.titulo,
                competencia = pagamento.competencia.toString()
                    .substring(0, 7),
                valor = pagamento.valorPrevisto,
                vencimento = pagamento.dataVencimento.toString(),
                status = pagamento.status,
                situacaoVencimento = situacao
            )
        }

        val nomeMes = periodo.month.getDisplayName(
            TextStyle.FULL,
            Locale("pt", "BR")
        ).replaceFirstChar { it.uppercase() }

        return DashboardResponse(
            competencia = periodo.toString(),
            mesAtual = "$nomeMes de ${periodo.year}",
            recebidos = recebidos,
            pendentes = pendentes,
            atrasados = atrasados,
            alugueis = alugueis
        )
    }
}