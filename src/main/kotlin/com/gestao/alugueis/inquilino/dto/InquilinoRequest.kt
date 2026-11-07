
package com.gestao.alugueis.inquilino.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class InquilinoRequest(

    @field:NotBlank(message = "O nome do inquilino é obrigatório.")
    @field:Size(max = 150)
    val nome: String,

    @field:NotBlank(message = "O CPF é obrigatório.")
    @field:Pattern(
        regexp = "^\\d{11}$|^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$",
        message = "Informe um CPF válido no formato 00000000000 ou 000.000.000-00."
    )
    val cpf: String,

    @field:Email(message = "Informe um e-mail válido.")
    @field:Size(max = 150)
    val email: String? = null,

    @field:NotBlank(message = "O telefone é obrigatório.")
    @field:Size(max = 20)
    val telefone: String,

    @field:Size(max = 200)
    val endereco: String? = null,

    @field:Size(max = 20)
    val numero: String? = null,

    @field:Size(max = 100)
    val complemento: String? = null,

    @field:Size(max = 100)
    val bairro: String? = null,

    @field:Size(max = 100)
    val cidade: String? = null,

    @field:Pattern(
        regexp = "^[A-Za-z]{2}$",
        message = "Informe uma UF válida com duas letras."
    )
    val estado: String? = null,

    @field:Size(max = 9)
    val cep: String? = null,

    val ativo: Boolean = true
)