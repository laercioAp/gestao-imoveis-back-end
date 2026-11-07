
package com.gestao.alugueis.recibo.service

import com.gestao.alugueis.pagamento.repository.PagamentoRepository
import com.gestao.alugueis.recibo.entity.Recibo
import com.gestao.alugueis.recibo.repository.ReciboRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class ReciboService(
    private val reciboRepository: ReciboRepository,
    private val pagamentoRepository: PagamentoRepository
) {

    @Transactional
    fun emitir(pagamentoId: Long): Recibo {

        val pagamento = pagamentoRepository.findById(pagamentoId)
            .orElseThrow {
                NoSuchElementException(
                    "Pagamento não encontrado: $pagamentoId"
                )
            }

        if (pagamento.status != "PAGO") {
            throw IllegalArgumentException(
                "Não é possível emitir recibo de um pagamento pendente."
            )
        }

        val reciboExistente =
            reciboRepository.findByPagamentoId(pagamentoId)

        if (reciboExistente != null) {
            return reciboExistente
        }

        val numeroRecibo = gerarNumeroRecibo()

        val recibo = Recibo(
            pagamento = pagamento,
            numeroRecibo = numeroRecibo,
            dataEmissao = LocalDateTime.now(),
            criadoEm = LocalDateTime.now()
        )

        return reciboRepository.save(recibo)
    }

    @Transactional(readOnly = true)
    fun buscarPorPagamento(pagamentoId: Long): Recibo {

        return reciboRepository.findByPagamentoId(pagamentoId)
            ?: throw NoSuchElementException(
                "Recibo não encontrado para o pagamento: $pagamentoId"
            )
    }

    @Transactional(readOnly = true)
    fun buscarPorNumero(numeroRecibo: String): Recibo {

        return reciboRepository.findByNumeroRecibo(numeroRecibo)
            ?: throw NoSuchElementException(
                "Recibo não encontrado: $numeroRecibo"
            )
    }

    private fun gerarNumeroRecibo(): String {

        val data = LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        )

        val codigo = java.util.UUID.randomUUID()
            .toString()
            .take(6)
            .uppercase()

        return "REC-$data-$codigo"
    }
}