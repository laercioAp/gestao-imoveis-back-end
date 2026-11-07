
package com.gestao.alugueis.usuario.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UsuarioRequest(

    @field:NotBlank(message = "O nome é obrigatório.")
    @field:Size(max = 150, message = "O nome deve ter no máximo 150 caracteres.")
    val nome: String,

    @field:NotBlank(message = "O e-mail é obrigatório.")
    @field:Email(message = "Informe um e-mail válido.")
    @field:Size(max = 150, message = "O e-mail deve ter no máximo 150 caracteres.")
    val email: String,

    @field:NotBlank(message = "A senha é obrigatória.")
    @field:Size(min = 8, max = 100, message = "A senha deve possuir entre 8 e 100 caracteres.")
    val senha: String
)