
package com.gestao.alugueis.imovel.dto

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.math.BigDecimal

data class ImovelRequest(

    @field:NotBlank(message = "O título do imóvel é obrigatório.")
    @field:Size(max = 150)
    val titulo: String,

    @field:NotBlank(message = "O tipo do imóvel é obrigatório.")
    @field:Size(max = 50)
    val tipo: String,

    val descricao: String? = null,

    @field:NotBlank(message = "O endereço é obrigatório.")
    @field:Size(max = 200)
    val endereco: String,

    @field:Size(max = 20)
    val numero: String? = null,

    @field:Size(max = 100)
    val complemento: String? = null,

    @field:Size(max = 100)
    val bairro: String? = null,

    @field:NotBlank(message = "A cidade é obrigatória.")
    @field:Size(max = 100)
    val cidade: String,

    @field:NotBlank(message = "O estado é obrigatório.")
    @field:Size(min = 2, max = 2, message = "Informe a UF com duas letras.")
    val estado: String,

    @field:Size(max = 9)
    val cep: String? = null,

    @field:DecimalMin(
        value = "0.01",
        message = "O valor do aluguel deve ser maior que zero."
    )
    val valorAluguel: BigDecimal,

    val ativo: Boolean = true
)
