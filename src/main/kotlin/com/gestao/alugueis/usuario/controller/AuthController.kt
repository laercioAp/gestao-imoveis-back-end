
package com.gestao.alugueis.usuario.controller

import com.gestao.alugueis.usuario.dto.LoginRequest
import com.gestao.alugueis.usuario.dto.LoginResponse
import com.gestao.alugueis.usuario.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest
    ): ResponseEntity<LoginResponse> {

        val response = authService.login(request)

        return ResponseEntity.ok(response)
    }
}