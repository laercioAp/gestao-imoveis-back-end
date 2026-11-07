
package com.gestao.alugueis.contrato.mapper

import com.gestao.alugueis.contrato.dto.ContratoClausulaRequest
import com.gestao.alugueis.contrato.dto.ContratoClausulaResponse
import com.gestao.alugueis.contrato.dto.ContratoRequest
import com.gestao.alugueis.contrato.dto.ContratoResponse
import com.gestao.alugueis.contrato.entity.Contrato
import com.gestao.alugueis.contrato.entity.ContratoClausula
import com.gestao.alugueis.imovel.entity.Imovel
import com.gestao.alugueis.inquilino.entity.Inquilino

object ContratoMapper {

    fun toEntity(
        request: ContratoRequest,
        imovel: Imovel,
        inquilino: Inquilino
    ): Contrato {
        return Contrato(
            imovel = imovel,
            inquilino = inquilino,
            finalidade = request.finalidade.trim(),
            valorAluguel = request.valorAluguel,
            diaVencimento = request.diaVencimento,
            dataInicio = request.dataInicio,
            dataFim = request.dataFim,
            indiceReajuste = request.indiceReajuste?.trim(),
            valorCaucao = request.valorCaucao,
            observacoes = request.observacoes?.trim(),
            locadorNome = request.locadorNome.trim(),
            locadorDocumento = request.locadorDocumento?.trim(),
            locadorEndereco = request.locadorEndereco?.trim(),
            status = request.status.trim().uppercase()
        )
    }

    fun clausulaToEntity(
        request: ContratoClausulaRequest,
        contrato: Contrato
    ): ContratoClausula {
        return ContratoClausula(
            contrato = contrato,
            ordem = request.ordem,
            titulo = request.titulo.trim(),
            conteudo = request.conteudo.trim(),
            ativa = request.ativa,
            personalizada = request.personalizada
        )
    }

    fun clausulaToResponse(
        clausula: ContratoClausula
    ): ContratoClausulaResponse {
        return ContratoClausulaResponse(
            id = clausula.id,
            ordem = clausula.ordem,
            titulo = clausula.titulo,
            conteudo = clausula.conteudo,
            ativa = clausula.ativa,
            personalizada = clausula.personalizada
        )
    }

    fun toResponse(
        contrato: Contrato,
        clausulas: List<ContratoClausula> = emptyList()
    ): ContratoResponse {
        return ContratoResponse(
            id = contrato.id,

            imovelId = contrato.imovel.id!!,
            imovelTitulo = contrato.imovel.titulo,

            inquilinoId = contrato.inquilino.id!!,
            inquilinoNome = contrato.inquilino.nome,
            inquilinoCpf = contrato.inquilino.cpf,

            finalidade = contrato.finalidade,
            valorAluguel = contrato.valorAluguel,
            diaVencimento = contrato.diaVencimento,

            dataInicio = contrato.dataInicio,
            dataFim = contrato.dataFim,

            indiceReajuste = contrato.indiceReajuste,
            valorCaucao = contrato.valorCaucao,

            observacoes = contrato.observacoes,

            locadorNome = contrato.locadorNome,
            locadorDocumento = contrato.locadorDocumento,
            locadorEndereco = contrato.locadorEndereco,

            status = contrato.status,

            criadoEm = contrato.criadoEm,
            atualizadoEm = contrato.atualizadoEm,

            clausulas = clausulas
                .sortedBy { it.ordem }
                .map(::clausulaToResponse)
        )
    }
}
