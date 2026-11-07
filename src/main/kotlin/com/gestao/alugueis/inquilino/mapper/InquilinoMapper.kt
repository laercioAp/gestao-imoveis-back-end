
package com.gestao.alugueis.inquilino.mapper

import com.gestao.alugueis.inquilino.dto.InquilinoRequest
import com.gestao.alugueis.inquilino.dto.InquilinoResponse
import com.gestao.alugueis.inquilino.entity.Inquilino

object InquilinoMapper {

    fun toEntity(request: InquilinoRequest): Inquilino {
        return Inquilino(
            nome = request.nome.trim(),
            cpf = request.cpf.filter { it.isDigit() },
            email = request.email?.trim()?.takeIf { it.isNotBlank() },
            telefone = request.telefone.trim(),
            endereco = request.endereco?.trim(),
            numero = request.numero?.trim(),
            complemento = request.complemento?.trim(),
            bairro = request.bairro?.trim(),
            cidade = request.cidade?.trim(),
            estado = request.estado?.trim()?.uppercase(),
            cep = request.cep?.trim(),
            ativo = request.ativo
        )
    }

    fun toResponse(inquilino: Inquilino): InquilinoResponse {
        return InquilinoResponse(
            id = inquilino.id,
            nome = inquilino.nome,
            cpf = inquilino.cpf,
            email = inquilino.email,
            telefone = inquilino.telefone,
            endereco = inquilino.endereco,
            numero = inquilino.numero,
            complemento = inquilino.complemento,
            bairro = inquilino.bairro,
            cidade = inquilino.cidade,
            estado = inquilino.estado,
            cep = inquilino.cep,
            ativo = inquilino.ativo,
            criadoEm = inquilino.criadoEm,
            atualizadoEm = inquilino.atualizadoEm
        )
    }
}