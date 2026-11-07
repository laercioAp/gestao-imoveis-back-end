
package com.gestao.alugueis.usuario.service

import com.gestao.alugueis.usuario.entity.Usuario
import com.gestao.alugueis.usuario.repository.UsuarioRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UsuarioService(
    private val usuarioRepository: UsuarioRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun cadastrar(
        nome: String,
        email: String,
        senha: String
    ): Usuario {

        require(nome.isNotBlank()) {
            "O nome é obrigatório."
        }

        require(email.isNotBlank()) {
            "O e-mail é obrigatório."
        }

        require(senha.length >= 8) {
            "A senha deve possuir pelo menos 8 caracteres."
        }

        val emailNormalizado = email.trim().lowercase()

        require(!usuarioRepository.existsByEmail(emailNormalizado)) {
            "Já existe um usuário cadastrado com este e-mail."
        }

        val senhaCriptografada = requireNotNull(passwordEncoder.encode(senha)) {
            "Não foi possível criptografar a senha."
        }

        val usuario = Usuario(
            nome = nome.trim(),
            email = emailNormalizado,
            senha = senhaCriptografada,
            ativo = true
        )

        return usuarioRepository.save(usuario)
    }

    @Transactional(readOnly = true)
    fun buscarPorEmail(email: String): Usuario {
        return usuarioRepository.findByEmail(email.trim().lowercase())
            ?: throw NoSuchElementException("Usuário não encontrado.")
    }
}
