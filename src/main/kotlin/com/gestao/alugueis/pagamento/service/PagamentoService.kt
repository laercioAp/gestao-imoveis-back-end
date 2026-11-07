
package com.gestao.alugueis.pagamento.service

import com.gestao.alugueis.contrato.repository.ContratoRepository
import com.gestao.alugueis.pagamento.dto.GerarPagamentosRequest
import com.gestao.alugueis.pagamento.dto.PagamentoRequest
import com.gestao.alugueis.pagamento.dto.PagamentoResponse
import com.gestao.alugueis.pagamento.entity.Pagamento
import com.gestao.alugueis.pagamento.mapper.PagamentoMapper
import com.gestao.alugueis.pagamento.repository.PagamentoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate
import java.time.YearMonth

@Service
class PagamentoService(
    private val pagamentoRepository: PagamentoRepository,
    private val contratoRepository: ContratoRepository
) {

    @Transactional(readOnly = true)
    fun listarTodos(): List<PagamentoResponse> =
        pagamentoRepository.findAll()
            .map(PagamentoMapper::toResponse)

    @Transactional(readOnly = true)
    fun listarPorId(id: Long): PagamentoResponse =
        PagamentoMapper.toResponse(buscarEntidade(id))

    @Transactional(readOnly = true)
    fun listarPorContrato(contratoId: Long): List<PagamentoResponse> =
        pagamentoRepository.findByContratoId(contratoId)
            .map(PagamentoMapper::toResponse)

    @Transactional(readOnly = true)
    fun listarPorStatus(status: String): List<PagamentoResponse> =
        pagamentoRepository.findByStatus(status.trim().uppercase())
            .map(PagamentoMapper::toResponse)

    @Transactional(readOnly = true)
    fun listarAtrasados(): List<PagamentoResponse> =
        pagamentoRepository.findByDataVencimentoBeforeAndStatus(
            LocalDate.now(),
            "PENDENTE"
        ).map(PagamentoMapper::toResponse)

    @Transactional
    fun cadastrar(request: PagamentoRequest): PagamentoResponse {
        validarDados(request)

        val contrato = contratoRepository.findById(request.contratoId)
            .orElseThrow {
                NoSuchElementException(
                    "Contrato não encontrado: ${request.contratoId}"
                )
            }

        if (contrato.status != "ATIVO") {
            throw IllegalArgumentException(
                "Só é possível gerar pagamentos para contratos ativos."
            )
        }

        val competencia = request.competencia.withDayOfMonth(1)

        if (
            pagamentoRepository.existsByContratoIdAndCompetencia(
                request.contratoId,
                competencia
            )
        ) {
            throw IllegalArgumentException(
                "Já existe um pagamento cadastrado para esta competência."
            )
        }

        val pagamento = PagamentoMapper.toEntity(
            request = request.copy(competencia = competencia),
            contrato = contrato
        )

        return PagamentoMapper.toResponse(
            pagamentoRepository.save(pagamento)
        )
    }

    @Transactional
    fun gerarPagamentosMensais(
        request: GerarPagamentosRequest
    ): Map<String, Any> {

        val competencia = request.competencia.withDayOfMonth(1)
        val periodo = YearMonth.from(competencia)

        /*
         * Quando contratoId é informado, busca somente aquele contrato.
         * Quando não é informado, mantém a geração para todos os ativos.
         */
        val contratosParaProcessar = if (request.contratoId != null) {

            val contrato = contratoRepository.findById(request.contratoId)
                .orElseThrow {
                    NoSuchElementException(
                        "Contrato não encontrado: ${request.contratoId}"
                    )
                }

            listOf(contrato)

        } else {

            contratoRepository.findByStatus("ATIVO")

        }

        var quantidadeGerada = 0
        var quantidadeJaExistente = 0
        var quantidadeContratosIgnorados = 0

        val pagamentosGerados = mutableListOf<PagamentoResponse>()

        val detalhes = mutableListOf<Map<String, Any?>>()

        contratosParaProcessar.forEach { contrato ->

            val contratoId = contrato.id!!

            // Verifica se o contrato está ativo.
            if (contrato.status != "ATIVO") {

                quantidadeContratosIgnorados++

                detalhes.add(
                    mapOf(
                        "contratoId" to contratoId,
                        "resultado" to "IGNORADO",
                        "motivo" to "O contrato não está ativo."
                    )
                )

                return@forEach
            }

            val inicioContrato = contrato.dataInicio
            val fimContrato = contrato.dataFim

            val contratoIniciado =
                !inicioContrato.isAfter(periodo.atEndOfMonth())

            val contratoAindaValido =
                fimContrato == null ||
                    !fimContrato.isBefore(periodo.atDay(1))

            if (!contratoIniciado) {

                quantidadeContratosIgnorados++

                detalhes.add(
                    mapOf(
                        "contratoId" to contratoId,
                        "resultado" to "IGNORADO",
                        "motivo" to
                            "O contrato ainda não havia iniciado na competência selecionada."
                    )
                )

                return@forEach
            }

            if (!contratoAindaValido) {

                quantidadeContratosIgnorados++

                detalhes.add(
                    mapOf(
                        "contratoId" to contratoId,
                        "resultado" to "IGNORADO",
                        "motivo" to
                            "O contrato já estava encerrado na competência selecionada."
                    )
                )

                return@forEach
            }

            // Evita duplicidade para o mesmo contrato e competência.
            val jaExiste =
                pagamentoRepository.existsByContratoIdAndCompetencia(
                    contratoId,
                    competencia
                )

            if (jaExiste) {

                quantidadeJaExistente++

                detalhes.add(
                    mapOf(
                        "contratoId" to contratoId,
                        "resultado" to "JA_EXISTENTE",
                        "motivo" to
                            "Já existe uma mensalidade para este contrato nesta competência."
                    )
                )

                return@forEach
            }

            // Calcula o vencimento respeitando o último dia do mês.
            val diaVencimento = minOf(
                contrato.diaVencimento,
                periodo.lengthOfMonth()
            )

            val dataVencimento = periodo.atDay(diaVencimento)

            val pagamento = Pagamento(
                contrato = contrato,
                competencia = competencia,
                valorPrevisto = contrato.valorAluguel,
                dataVencimento = dataVencimento,
                status = "PENDENTE"
            )

            val salvo = pagamentoRepository.save(pagamento)

            val pagamentoResponse = PagamentoMapper.toResponse(salvo)

            pagamentosGerados.add(pagamentoResponse)

            quantidadeGerada++

            detalhes.add(
                mapOf(
                    "contratoId" to contratoId,
                    "resultado" to "GERADO",
                    "motivo" to "Mensalidade gerada com sucesso."
                )
            )
        }

        return mapOf(
            "competencia" to competencia,
            "quantidadeGerada" to quantidadeGerada,
            "quantidadeJaExistente" to quantidadeJaExistente,
            "quantidadeContratosIgnorados" to quantidadeContratosIgnorados,
            "pagamentos" to pagamentosGerados,
            "detalhes" to detalhes
        )
    }

    @Transactional
    fun registrarPagamento(
        id: Long,
        valorPago: BigDecimal,
        dataPagamento: LocalDate,
        formaPagamento: String
    ): PagamentoResponse {

        val pagamento = buscarEntidade(id)

        if (pagamento.status == "PAGO") {
            throw IllegalArgumentException(
                "Este pagamento já foi registrado como pago."
            )
        }

        if (valorPago.signum() <= 0) {
            throw IllegalArgumentException(
                "O valor pago deve ser maior que zero."
            )
        }

        if (dataPagamento.isAfter(LocalDate.now())) {
            throw IllegalArgumentException(
                "A data do pagamento não pode ser futura."
            )
        }

        if (formaPagamento.isBlank()) {
            throw IllegalArgumentException(
                "A forma de pagamento é obrigatória."
            )
        }

        pagamento.valorPago = valorPago
        pagamento.dataPagamento = dataPagamento
        pagamento.formaPagamento = formaPagamento.trim()
        pagamento.status = "PAGO"

        return PagamentoMapper.toResponse(
            pagamentoRepository.save(pagamento)
        )
    }

    @Transactional
    fun excluir(id: Long) {
        val pagamento = buscarEntidade(id)

        if (pagamento.status == "PAGO") {
            throw IllegalArgumentException(
                "Não é possível excluir um pagamento já realizado."
            )
        }

        pagamentoRepository.delete(pagamento)
    }

    private fun validarDados(request: PagamentoRequest) {

        if (request.valorPrevisto.signum() <= 0) {
            throw IllegalArgumentException(
                "O valor previsto deve ser maior que zero."
            )
        }

        if (
            request.dataPagamento != null &&
            request.dataPagamento.isAfter(LocalDate.now())
        ) {
            throw IllegalArgumentException(
                "A data do pagamento não pode ser futura."
            )
        }

        if (
            request.status.trim().uppercase() !in
            listOf("PENDENTE", "PAGO")
        ) {
            throw IllegalArgumentException(
                "Status do pagamento inválido."
            )
        }

        if (request.status.trim().uppercase() == "PAGO") {

            if (
                request.valorPago == null ||
                request.valorPago.signum() <= 0 ||
                request.dataPagamento == null ||
                request.formaPagamento.isNullOrBlank()
            ) {
                throw IllegalArgumentException(
                    "Para cadastrar como pago, informe valor, data e forma de pagamento."
                )
            }
        }
    }

    private fun buscarEntidade(id: Long): Pagamento =
        pagamentoRepository.findById(id).orElseThrow {
            NoSuchElementException(
                "Pagamento não encontrado: $id"
            )
        }
}