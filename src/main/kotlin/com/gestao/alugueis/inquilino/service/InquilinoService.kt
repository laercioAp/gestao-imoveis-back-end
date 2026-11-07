
package com.gestao.alugueis.inquilino.service

import com.gestao.alugueis.inquilino.dto.InquilinoRequest
import com.gestao.alugueis.inquilino.dto.InquilinoResponse
import com.gestao.alugueis.inquilino.entity.Inquilino
import com.gestao.alugueis.inquilino.mapper.InquilinoMapper
import com.gestao.alugueis.inquilino.repository.InquilinoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InquilinoService(
    private val inquilinoRepository: InquilinoRepository
) {

    fun listarTodos(): List<InquilinoResponse> =
        inquilinoRepository.findAll().map(InquilinoMapper::toResponse)

    fun listarAtivos(): List<InquilinoResponse> =
        inquilinoRepository.findByAtivo(true).map(InquilinoMapper::toResponse)

    fun buscarPorId(id: Long): InquilinoResponse =
        InquilinoMapper.toResponse(buscarEntidade(id))

    @Transactional
    fun cadastrar(request: InquilinoRequest): InquilinoResponse {
        val cpf = request.cpf.filter { it.isDigit() }

        if (inquilinoRepository.existsByCpf(cpf)) {
            throw IllegalArgumentException("Já existe um inquilino cadastrado com este CPF.")
        }

        val inquilino = InquilinoMapper.toEntity(request)

        return InquilinoMapper.toResponse(
            inquilinoRepository.save(inquilino)
        )
    }

    @Transactional
    fun atualizar(id: Long, request: InquilinoRequest): InquilinoResponse {
        val inquilino = buscarEntidade(id)
        val cpf = request.cpf.filter { it.isDigit() }

        val cpfEmUso = inquilinoRepository.findByCpf(cpf)

        if (cpfEmUso != null && cpfEmUso.id != id) {
            throw IllegalArgumentException("Já existe outro inquilino cadastrado com este CPF.")
        }

        val dados = InquilinoMapper.toEntity(request)

        inquilino.nome = dados.nome
        inquilino.cpf = dados.cpf
        inquilino.email = dados.email
        inquilino.telefone = dados.telefone
        inquilino.endereco = dados.endereco
        inquilino.numero = dados.numero
        inquilino.complemento = dados.complemento
        inquilino.bairro = dados.bairro
        inquilino.cidade = dados.cidade
        inquilino.estado = dados.estado
        inquilino.cep = dados.cep
        inquilino.ativo = dados.ativo

        return InquilinoMapper.toResponse(
            inquilinoRepository.save(inquilino)
        )
    }

    @Transactional
    fun excluir(id: Long) {
        val inquilino = buscarEntidade(id)
        inquilinoRepository.delete(inquilino)
    }

    private fun buscarEntidade(id: Long): Inquilino =
        inquilinoRepository.findById(id).orElseThrow {
            NoSuchElementException("Inquilino não encontrado: $id")
        }
}