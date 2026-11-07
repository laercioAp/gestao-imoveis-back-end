
package com.gestao.alugueis.pagamento.mapper

import com.gestao.alugueis.contrato.entity.Contrato
import com.gestao.alugueis.pagamento.dto.PagamentoRequest
import com.gestao.alugueis.pagamento.dto.PagamentoResponse
import com.gestao.alugueis.pagamento.entity.Pagamento
import java.time.LocalDate

object PagamentoMapper {

    fun toEntity(
        request: PagamentoRequest,
        contrato: Contrato
    ): Pagamento {
        val status = request.status.trim().uppercase()

        return Pagamento(
            contrato = contrato,
            competencia = request.competencia,
            valorPrevisto = request.valorPrevisto,
            valorPago = request.valorPago,
            dataVencimento = request.dataVencimento,
            dataPagamento = request.dataPagamento,
            formaPagamento = request.formaPagamento?.trim(),
            status = status,
            observacoes = request.observacoes?.trim()
        )
    }

    fun toResponse(pagamento: Pagamento): PagamentoResponse {

        val situacaoVencimento = when {
            pagamento.status == "PAGO" -> "PAGO"

            pagamento.dataVencimento.isBefore(LocalDate.now()) ->
                "ATRASADO"

            else -> "PENDENTE"
        }

        return PagamentoResponse(
            id = pagamento.id,
            contratoId = pagamento.contrato.id!!,
            imovelId = pagamento.contrato.imovel.id!!,
            imovelTitulo = pagamento.contrato.imovel.titulo,
            inquilinoId = pagamento.contrato.inquilino.id!!,
            inquilinoNome = pagamento.contrato.inquilino.nome,
            competencia = pagamento.competencia,
            valorPrevisto = pagamento.valorPrevisto,
            valorPago = pagamento.valorPago,
            dataVencimento = pagamento.dataVencimento,
            dataPagamento = pagamento.dataPagamento,
            formaPagamento = pagamento.formaPagamento,
            status = pagamento.status,
            situacaoVencimento = situacaoVencimento,
            observacoes = pagamento.observacoes,
            criadoEm = pagamento.criadoEm,
            atualizadoEm = pagamento.atualizadoEm
        )
    }
}