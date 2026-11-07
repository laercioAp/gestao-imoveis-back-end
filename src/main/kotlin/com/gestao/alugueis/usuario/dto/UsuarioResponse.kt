
package com.gestao.alugueis.usuario.dto

import java.time.LocalDateTime

data class UsuarioResponse(
    val id: Long?,
    val nome: String,
    val email: String,
    val ativo: Boolean,
    val criadoEm: LocalDateTime?
)