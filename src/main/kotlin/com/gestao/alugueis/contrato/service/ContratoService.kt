
package com.gestao.alugueis.contrato.service

import com.gestao.alugueis.contrato.dto.ContratoRequest
import com.gestao.alugueis.contrato.dto.ContratoResponse
import com.gestao.alugueis.contrato.entity.Contrato
import com.gestao.alugueis.contrato.mapper.ContratoMapper
import com.gestao.alugueis.contrato.repository.ContratoClausulaRepository
import com.gestao.alugueis.contrato.repository.ContratoRepository
import com.gestao.alugueis.imovel.repository.ImovelRepository
import com.gestao.alugueis.inquilino.repository.InquilinoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ContratoService(
    private val contratoRepository: ContratoRepository,
    private val contratoClausulaRepository: ContratoClausulaRepository,
    private val imovelRepository: ImovelRepository,
    private val inquilinoRepository: InquilinoRepository
) {

    @Transactional(readOnly = true)
    fun listarTodos(): List<ContratoResponse> =
        contratoRepository.findAll()
            .map { contrato ->
                montarResposta(contrato)
            }

    @Transactional(readOnly = true)
    fun listarPorStatus(status: String): List<ContratoResponse> =
        contratoRepository.findByStatus(status.trim().uppercase())
            .map { contrato ->
                montarResposta(contrato)
            }

    @Transactional(readOnly = true)
    fun listarPorImovel(imovelId: Long): List<ContratoResponse> =
        contratoRepository.findByImovelId(imovelId)
            .map { contrato ->
                montarResposta(contrato)
            }

    @Transactional(readOnly = true)
    fun listarPorInquilino(inquilinoId: Long): List<ContratoResponse> =
        contratoRepository.findByInquilinoId(inquilinoId)
            .map { contrato ->
                montarResposta(contrato)
            }

    @Transactional(readOnly = true)
    fun buscarPorId(id: Long): ContratoResponse =
        montarResposta(buscarEntidade(id))

    @Transactional
    fun cadastrar(request: ContratoRequest): ContratoResponse {
        validarDados(request)

        val imovel = imovelRepository.findById(request.imovelId)
            .orElseThrow {
                NoSuchElementException("Imóvel não encontrado: ${request.imovelId}")
            }

        val inquilino = inquilinoRepository.findById(request.inquilinoId)
            .orElseThrow {
                NoSuchElementException("Inquilino não encontrado: ${request.inquilinoId}")
            }

        if (!imovel.ativo) {
            throw IllegalArgumentException(
                "Não é possível contratar um imóvel inativo."
            )
        }

        if (!inquilino.ativo) {
            throw IllegalArgumentException(
                "Não é possível vincular um inquilino inativo."
            )
        }

        validarDisponibilidadeImovel(
            imovelId = request.imovelId,
            status = request.status
        )

        val contrato = contratoRepository.save(
            ContratoMapper.toEntity(
                request = request,
                imovel = imovel,
                inquilino = inquilino
            )
        )

        salvarClausulas(contrato, request)

        return montarResposta(contrato)
    }

    @Transactional
    fun atualizar(
        id: Long,
        request: ContratoRequest
    ): ContratoResponse {
        validarDados(request)

        val contrato = buscarEntidade(id)

        val imovel = imovelRepository.findById(request.imovelId)
            .orElseThrow {
                NoSuchElementException("Imóvel não encontrado: ${request.imovelId}")
            }

        val inquilino = inquilinoRepository.findById(request.inquilinoId)
            .orElseThrow {
                NoSuchElementException("Inquilino não encontrado: ${request.inquilinoId}")
            }

        if (!imovel.ativo) {
            throw IllegalArgumentException(
                "Não é possível utilizar um imóvel inativo."
            )
        }

        if (!inquilino.ativo) {
            throw IllegalArgumentException(
                "Não é possível utilizar um inquilino inativo."
            )
        }

        validarDisponibilidadeImovel(
            imovelId = request.imovelId,
            status = request.status,
            contratoAtualId = id
        )

        contrato.imovel = imovel
        contrato.inquilino = inquilino
        contrato.finalidade = request.finalidade.trim()
        contrato.valorAluguel = request.valorAluguel
        contrato.diaVencimento = request.diaVencimento
        contrato.dataInicio = request.dataInicio
        contrato.dataFim = request.dataFim
        contrato.indiceReajuste = request.indiceReajuste?.trim()
        contrato.valorCaucao = request.valorCaucao
        contrato.observacoes = request.observacoes?.trim()

        contrato.locadorNome = request.locadorNome.trim()
        contrato.locadorDocumento = request.locadorDocumento?.trim()
        contrato.locadorEndereco = request.locadorEndereco?.trim()

        contrato.status = request.status.trim().uppercase()

        contratoRepository.save(contrato)

        if (request.clausulas.isNotEmpty()) {
            contratoClausulaRepository.deleteByContratoId(id)
            salvarClausulas(contrato, request)
        }

        return montarResposta(contrato)
    }

    @Transactional
    fun excluir(id: Long) {
        val contrato = buscarEntidade(id)
        contratoRepository.delete(contrato)
    }

    private fun salvarClausulas(
        contrato: Contrato,
        request: ContratoRequest
    ) {
        val clausulas = request.clausulas.map { clausula ->
            ContratoMapper.clausulaToEntity(
                request = clausula,
                contrato = contrato
            )
        }

        if (clausulas.isNotEmpty()) {
            contratoClausulaRepository.saveAll(clausulas)
        }
    }

    private fun montarResposta(contrato: Contrato): ContratoResponse {
        val id = requireNotNull(contrato.id) {
            "O contrato ainda não possui identificador."
        }

        val clausulas =
            contratoClausulaRepository.findByContratoIdOrderByOrdemAsc(id)

        return ContratoMapper.toResponse(contrato, clausulas)
    }

    private fun validarDados(request: ContratoRequest) {
        val status = request.status.trim().uppercase()

        if (status !in listOf("RASCUNHO", "ATIVO", "ENCERRADO")) {
            throw IllegalArgumentException("Status do contrato inválido.")
        }

        if (request.locadorNome.isBlank()) {
            throw IllegalArgumentException(
                "O nome ou razão social do locador é obrigatório."
            )
        }

        if (request.locadorNome.length > 200) {
            throw IllegalArgumentException(
                "O nome do locador deve ter no máximo 200 caracteres."
            )
        }

        if (
            request.dataFim != null &&
            request.dataFim.isBefore(request.dataInicio)
        ) {
            throw IllegalArgumentException(
                "A data final não pode ser anterior à data inicial."
            )
        }

        if (
            request.valorCaucao != null &&
            request.valorCaucao.signum() < 0
        ) {
            throw IllegalArgumentException(
                "O valor da caução não pode ser negativo."
            )
        }

        val ordens = request.clausulas.map { it.ordem }

        if (ordens.distinct().size != ordens.size) {
            throw IllegalArgumentException(
                "A ordem das cláusulas não pode se repetir."
            )
        }

        if (
            request.clausulas.any {
                it.titulo.isBlank() ||
                it.conteudo.isBlank() ||
                it.ordem <= 0
            }
        ) {
            throw IllegalArgumentException(
                "Verifique o título, o conteúdo e a ordem das cláusulas."
            )
        }
    }

    private fun validarDisponibilidadeImovel(
        imovelId: Long,
        status: String,
        contratoAtualId: Long? = null
    ) {
        if (status.trim().uppercase() != "ATIVO") {
            return
        }

        val possuiOutroContratoAtivo = contratoRepository
            .findByImovelId(imovelId)
            .any {
                it.status == "ATIVO" && it.id != contratoAtualId
            }

        if (possuiOutroContratoAtivo) {
            throw IllegalArgumentException(
                "Este imóvel já possui um contrato ativo."
            )
        }
    }

    private fun buscarEntidade(id: Long): Contrato =
        contratoRepository.findById(id).orElseThrow {
            NoSuchElementException("Contrato não encontrado: $id")
        }
}