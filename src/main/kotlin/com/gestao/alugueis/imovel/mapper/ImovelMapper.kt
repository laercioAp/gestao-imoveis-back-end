
package com.gestao.alugueis.imovel.mapper

import com.gestao.alugueis.imovel.dto.ImovelRequest
import com.gestao.alugueis.imovel.dto.ImovelResponse
import com.gestao.alugueis.imovel.entity.Imovel

object ImovelMapper {

    fun toEntity(request: ImovelRequest): Imovel {
        return Imovel(
            titulo = request.titulo.trim(),
            tipo = request.tipo.trim(),
            descricao = request.descricao,
            endereco = request.endereco.trim(),
            numero = request.numero,
            complemento = request.complemento,
            bairro = request.bairro,
            cidade = request.cidade.trim(),
            estado = request.estado.trim().uppercase(),
            cep = request.cep,
            valorAluguel = request.valorAluguel,
            ativo = request.ativo
        )
    }

    fun toResponse(imovel: Imovel): ImovelResponse {
        return ImovelResponse(
            id = imovel.id,
            titulo = imovel.titulo,
            tipo = imovel.tipo,
            descricao = imovel.descricao,
            endereco = imovel.endereco,
            numero = imovel.numero,
            complemento = imovel.complemento,
            bairro = imovel.bairro,
            cidade = imovel.cidade,
            estado = imovel.estado.trim(),
            cep = imovel.cep,
            valorAluguel = imovel.valorAluguel,
            ativo = imovel.ativo,
            criadoEm = imovel.criadoEm,
            atualizadoEm = imovel.atualizadoEm
        )
    }
}