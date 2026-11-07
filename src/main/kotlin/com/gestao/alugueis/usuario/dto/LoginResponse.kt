
package com.gestao.alugueis.usuario.dto

data class LoginResponse(
    val token: String,
    val tipo: String = "Bearer",
    val nome: String,
    val email: String
)
