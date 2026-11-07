
package com.gestao.alugueis.usuario.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "usuarios")
class Usuario(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 150)
    var nome: String,

    @Column(nullable = false, length = 150, unique = true)
    var email: String,

    @Column(nullable = false, length = 255)
    var senha: String,

    @Column(nullable = false)
    var ativo: Boolean = true,

    @Column(name = "criado_em", nullable = false)
    var criadoEm: LocalDateTime? = null,

    @Column(name = "atualizado_em", nullable = false)
    var atualizadoEm: LocalDateTime? = null
) {

    @PrePersist
    fun prePersist() {
        val agora = LocalDateTime.now()
        criadoEm = agora
        atualizadoEm = agora
    }

    @PreUpdate
    fun preUpdate() {
        atualizadoEm = LocalDateTime.now()
    }
}