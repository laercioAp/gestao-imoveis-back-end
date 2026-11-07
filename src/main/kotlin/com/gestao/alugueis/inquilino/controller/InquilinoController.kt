
package com.gestao.alugueis.inquilino.controller

import com.gestao.alugueis.inquilino.dto.InquilinoRequest
import com.gestao.alugueis.inquilino.dto.InquilinoResponse
import com.gestao.alugueis.inquilino.service.InquilinoService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/inquilinos")
@CrossOrigin(origins = ["http://localhost:4200"])
class InquilinoController(
    private val inquilinoService: InquilinoService
) {

    @GetMapping
    fun listarTodos(): ResponseEntity<List<InquilinoResponse>> =
        ResponseEntity.ok(inquilinoService.listarTodos())

    @GetMapping("/ativos")
    fun listarAtivos(): ResponseEntity<List<InquilinoResponse>> =
        ResponseEntity.ok(inquilinoService.listarAtivos())

    @GetMapping("/{id}")
    fun buscarPorId(
        @PathVariable id: Long
    ): ResponseEntity<InquilinoResponse> =
        ResponseEntity.ok(inquilinoService.buscarPorId(id))

    @PostMapping
    fun cadastrar(
        @Valid @RequestBody request: InquilinoRequest
    ): ResponseEntity<InquilinoResponse> {
        val novoInquilino = inquilinoService.cadastrar(request)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(novoInquilino)
    }

    @PutMapping("/{id}")
    fun atualizar(
        @PathVariable id: Long,
        @Valid @RequestBody request: InquilinoRequest
    ): ResponseEntity<InquilinoResponse> =
        ResponseEntity.ok(inquilinoService.atualizar(id, request))

    @DeleteMapping("/{id}")
    fun excluir(
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        inquilinoService.excluir(id)
        return ResponseEntity.noContent().build()
    }
}