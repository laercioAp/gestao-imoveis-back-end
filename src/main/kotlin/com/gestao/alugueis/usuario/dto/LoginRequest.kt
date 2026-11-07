
package com.gestao.alugueis.usuario.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class LoginRequest(

    @field:NotBlank(message = "O e-mail é obrigatório.")
    @field:Email(message = "Informe um e-mail válido.")
    val email: String,

    @field:NotBlank(message = "A senha é obrigatória.")
    val senha: String
)