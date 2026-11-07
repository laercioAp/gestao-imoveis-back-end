
package com.gestao.alugueis.contrato.controller

import com.gestao.alugueis.contrato.dto.ContratoRequest
import com.gestao.alugueis.contrato.dto.ContratoResponse
import com.gestao.alugueis.contrato.service.ContratoService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/contratos")
@CrossOrigin(origins = ["http://localhost:4200"])
class ContratoController(
    private val contratoService: ContratoService
) {

    @GetMapping
    fun listarTodos(): ResponseEntity<List<ContratoResponse>> =
        ResponseEntity.ok(contratoService.listarTodos())

    @GetMapping("/status/{status}")
    fun listarPorStatus(
        @PathVariable status: String
    ): ResponseEntity<List<ContratoResponse>> =
        ResponseEntity.ok(contratoService.listarPorStatus(status))

    @GetMapping("/imovel/{imovelId}")
    fun listarPorImovel(
        @PathVariable imovelId: Long
    ): ResponseEntity<List<ContratoResponse>> =
        ResponseEntity.ok(contratoService.listarPorImovel(imovelId))

    @GetMapping("/inquilino/{inquilinoId}")
    fun listarPorInquilino(
        @PathVariable inquilinoId: Long
    ): ResponseEntity<List<ContratoResponse>> =
        ResponseEntity.ok(contratoService.listarPorInquilino(inquilinoId))

    @GetMapping("/{id}")
    fun buscarPorId(
        @PathVariable id: Long
    ): ResponseEntity<ContratoResponse> =
        ResponseEntity.ok(contratoService.buscarPorId(id))

    @PostMapping
    fun cadastrar(
        @Valid @RequestBody request: ContratoRequest
    ): ResponseEntity<ContratoResponse> {
        val novoContrato = contratoService.cadastrar(request)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(novoContrato)
    }

    @PutMapping("/{id}")
    fun atualizar(
        @PathVariable id: Long,
        @Valid @RequestBody request: ContratoRequest
    ): ResponseEntity<ContratoResponse> =
        ResponseEntity.ok(contratoService.atualizar(id, request))

    @DeleteMapping("/{id}")
    fun excluir(
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        contratoService.excluir(id)
        return ResponseEntity.noContent().build()
    }
}