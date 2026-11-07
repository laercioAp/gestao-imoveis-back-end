
package com.gestao.alugueis.usuario.service

import com.gestao.alugueis.usuario.repository.UsuarioRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UsuarioDetailsService(
    private val usuarioRepository: UsuarioRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {

        val email = username.trim().lowercase()

        val usuario = usuarioRepository.findByEmail(email)
            ?: throw UsernameNotFoundException("Usuário não encontrado.")

        if (!usuario.ativo) {
            throw UsernameNotFoundException("Usuário inativo.")
        }

        return User.withUsername(usuario.email)
            .password(usuario.senha)
            .authorities("ROLE_USER")
            .build()
    }
}