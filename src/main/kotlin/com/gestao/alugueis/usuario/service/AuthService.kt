
package com.gestao.alugueis.usuario.service

import com.gestao.alugueis.config.JwtService
import com.gestao.alugueis.usuario.dto.LoginRequest
import com.gestao.alugueis.usuario.dto.LoginResponse
import com.gestao.alugueis.usuario.repository.UsuarioRepository
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val usuarioRepository: UsuarioRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {

    @Transactional(readOnly = true)
    fun login(request: LoginRequest): LoginResponse {

        val email = request.email.trim().lowercase()

        val usuario = usuarioRepository.findByEmail(email)
            ?: throw BadCredentialsException("E-mail ou senha inválidos.")

        if (!usuario.ativo) {
            throw BadCredentialsException("E-mail ou senha inválidos.")
        }

        val senhaValida = passwordEncoder.matches(
            request.senha,
            usuario.senha
        )

        if (!senhaValida) {
            throw BadCredentialsException("E-mail ou senha inválidos.")
        }

        val token = jwtService.gerarToken(usuario.email)

        return LoginResponse(
            token = token,
            nome = usuario.nome,
            email = usuario.email
        )
    }
}