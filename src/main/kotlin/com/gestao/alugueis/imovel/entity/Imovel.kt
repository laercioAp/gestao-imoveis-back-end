
package com.gestao.alugueis.imovel.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "imoveis")
class Imovel(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 150)
    var titulo: String,

    @Column(nullable = false, length = 50)
    var tipo: String,

    @Column(columnDefinition = "TEXT")
    var descricao: String? = null,

    @Column(nullable = false, length = 200)
    var endereco: String,

    @Column(length = 20)
    var numero: String? = null,

    @Column(length = 100)
    var complemento: String? = null,

    @Column(length = 100)
    var bairro: String? = null,

    @Column(nullable = false, length = 100)
    var cidade: String,

    @Column(nullable = false, length = 2)
    var estado: String,

    @Column(length = 9)
    var cep: String? = null,

    @Column(name = "valor_aluguel", nullable = false, precision = 12, scale = 2)
    var valorAluguel: BigDecimal,

    @Column(nullable = false)
    var ativo: Boolean = true

) {

    @Column(name = "criado_em", nullable = false, updatable = false)
    var criadoEm: LocalDateTime = LocalDateTime.now()
        private set

    @Column(name = "atualizado_em", nullable = false)
    var atualizadoEm: LocalDateTime = LocalDateTime.now()
        private set

    @PrePersist
    fun antesDeSalvar() {
        val agora = LocalDateTime.now()
        criadoEm = agora
        atualizadoEm = agora
    }

    @PreUpdate
    fun antesDeAtualizar() {
        atualizadoEm = LocalDateTime.now()
    }
}
