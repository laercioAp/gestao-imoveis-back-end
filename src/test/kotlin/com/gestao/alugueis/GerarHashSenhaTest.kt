package com.gestao.alugueis

import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class GerarHashSenhaTest {

    @Test
    fun gerarHashSenha() {

        val novaSenha = "Laercio123"

        val encoder = BCryptPasswordEncoder()

        val hash = encoder.encode(novaSenha)

        println("NOVO HASH: $hash")

        println(
            "Senha validada: ${encoder.matches(novaSenha, hash)}"
        )
    }
}