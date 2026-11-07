
package com.gestao.alugueis.imovel.controller

import com.gestao.alugueis.imovel.dto.ImovelRequest
import com.gestao.alugueis.imovel.dto.ImovelResponse
import com.gestao.alugueis.imovel.service.ImovelService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/imoveis")
@CrossOrigin(origins = ["http://localhost:4200"])
class ImovelController(
    private val imovelService: ImovelService
) {

    @GetMapping
    fun listarTodos(): ResponseEntity<List<ImovelResponse>> =
        ResponseEntity.ok(imovelService.listarTodos())

    @GetMapping("/ativos")
    fun listarAtivos(): ResponseEntity<List<ImovelResponse>> =
        ResponseEntity.ok(imovelService.listarAtivos())

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long): ResponseEntity<ImovelResponse> =
        ResponseEntity.ok(imovelService.buscarPorId(id))

    @PostMapping
    fun cadastrar(
        @Valid @RequestBody request: ImovelRequest
    ): ResponseEntity<ImovelResponse> {
        val novoImovel = imovelService.cadastrar(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(novoImovel)
    }

    @PutMapping("/{id}")
    fun atualizar(
        @PathVariable id: Long,
        @Valid @RequestBody request: ImovelRequest
    ): ResponseEntity<ImovelResponse> =
        ResponseEntity.ok(imovelService.atualizar(id, request))

    @DeleteMapping("/{id}")
    fun excluir(@PathVariable id: Long): ResponseEntity<Void> {
        imovelService.excluir(id)
        return ResponseEntity.noContent().build()
    }
}