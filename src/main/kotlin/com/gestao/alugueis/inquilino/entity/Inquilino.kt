
package com.gestao.alugueis.inquilino.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "inquilinos",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_inquilinos_cpf",
            columnNames = ["cpf"]
        )
    ]
)
class Inquilino(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 150)
    var nome: String,

    @Column(nullable = false, length = 14)
    var cpf: String,

    @Column(length = 150)
    var email: String? = null,

    @Column(nullable = false, length = 20)
    var telefone: String,

    @Column(length = 200)
    var endereco: String? = null,

    @Column(length = 20)
    var numero: String? = null,

    @Column(length = 100)
    var complemento: String? = null,

    @Column(length = 100)
    var bairro: String? = null,

    @Column(length = 100)
    var cidade: String? = null,

    @Column(length = 2)
    var estado: String? = null,

    @Column(length = 9)
    var cep: String? = null,

    @Column(nullable = false)
    var ativo: Boolean = true,

    @Column(name = "criado_em", nullable = false)
    var criadoEm: LocalDateTime? = null,

    @Column(name = "atualizado_em", nullable = false)
    var atualizadoEm: LocalDateTime? = null
) {

    @PrePersist
    fun antesDeInserir() {
        val agora = LocalDateTime.now()
        criadoEm = agora
        atualizadoEm = agora
    }

    @PreUpdate
    fun antesDeAtualizar() {
        atualizadoEm = LocalDateTime.now()
    }
}