
package com.gestao.alugueis.imovel.service

import com.gestao.alugueis.imovel.dto.ImovelRequest
import com.gestao.alugueis.imovel.dto.ImovelResponse
import com.gestao.alugueis.imovel.mapper.ImovelMapper
import com.gestao.alugueis.imovel.repository.ImovelRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ImovelService(
    private val imovelRepository: ImovelRepository
) {

    fun listarTodos(): List<ImovelResponse> =
        imovelRepository.findAll().map(ImovelMapper::toResponse)

    fun listarAtivos(): List<ImovelResponse> =
        imovelRepository.findByAtivo(true).map(ImovelMapper::toResponse)

    fun buscarPorId(id: Long): ImovelResponse =
        ImovelMapper.toResponse(buscarEntidade(id))

    @Transactional
    fun cadastrar(request: ImovelRequest): ImovelResponse {
        val imovel = ImovelMapper.toEntity(request)
        return ImovelMapper.toResponse(imovelRepository.save(imovel))
    }

    @Transactional
    fun atualizar(id: Long, request: ImovelRequest): ImovelResponse {
        val imovel = buscarEntidade(id)
        val dados = ImovelMapper.toEntity(request)

        imovel.titulo = dados.titulo
        imovel.tipo = dados.tipo
        imovel.descricao = dados.descricao
        imovel.endereco = dados.endereco
        imovel.numero = dados.numero
        imovel.complemento = dados.complemento
        imovel.bairro = dados.bairro
        imovel.cidade = dados.cidade
        imovel.estado = dados.estado
        imovel.cep = dados.cep
        imovel.valorAluguel = dados.valorAluguel
        imovel.ativo = dados.ativo

        return ImovelMapper.toResponse(imovelRepository.save(imovel))
    }

    @Transactional
    fun excluir(id: Long) {
        val imovel = buscarEntidade(id)
        imovelRepository.delete(imovel)
    }

    private fun buscarEntidade(id: Long) =
        imovelRepository.findById(id).orElseThrow {
            NoSuchElementException("Imóvel não encontrado: $id")
        }
}