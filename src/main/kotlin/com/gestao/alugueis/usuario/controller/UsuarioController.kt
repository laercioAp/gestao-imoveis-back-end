
package com.gestao.alugueis.usuario.controller

import com.gestao.alugueis.usuario.dto.UsuarioRequest
import com.gestao.alugueis.usuario.dto.UsuarioResponse
import com.gestao.alugueis.usuario.service.UsuarioService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/usuarios")
class UsuarioController(
    private val usuarioService: UsuarioService
) {

    @PostMapping("/cadastro")
    fun cadastrar(
        @Valid @RequestBody request: UsuarioRequest
    ): ResponseEntity<UsuarioResponse> {

        val usuario = usuarioService.cadastrar(
            nome = request.nome,
            email = request.email,
            senha = request.senha
        )

        val response = UsuarioResponse(
            id = usuario.id,
            nome = usuario.nome,
            email = usuario.email,
            ativo = usuario.ativo,
            criadoEm = usuario.criadoEm
        )

        return ResponseEntity.status(HttpStatus.CREATED).body(response)
    }
}