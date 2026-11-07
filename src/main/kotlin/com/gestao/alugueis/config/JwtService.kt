
package com.gestao.alugueis.config

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.io.Decoders
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService(

    @Value("\${jwt.secret}")
    private val secret: String,

    @Value("\${jwt.expiration}")
    private val expiration: Long

) {

    private fun getSigningKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(secret)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun gerarToken(email: String): String {
        val agora = Date()
        val expiracao = Date(agora.time + expiration)

        return Jwts.builder()
            .subject(email)
            .issuedAt(agora)
            .expiration(expiracao)
            .signWith(getSigningKey())
            .compact()
    }

    fun extrairEmail(token: String): String {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .payload
            .subject
    }

    fun tokenValido(token: String, email: String): Boolean {
        return try {
            val emailToken = extrairEmail(token)
            emailToken == email
        } catch (ex: Exception) {
            false
        }
    }
}
