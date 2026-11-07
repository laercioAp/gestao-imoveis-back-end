
package com.gestao.alugueis.recibo.entity

import com.gestao.alugueis.pagamento.entity.Pagamento
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "recibos",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_recibos_pagamento",
            columnNames = ["pagamento_id"]
        )
    ]
)
class Recibo(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pagamento_id", nullable = false)
    var pagamento: Pagamento,

    @Column(name = "numero_recibo", nullable = false, unique = true, length = 30)
    var numeroRecibo: String,

    @Column(name = "data_emissao", nullable = false)
    var dataEmissao: LocalDateTime? = null,

    @Column(name = "criado_em", nullable = false)
    var criadoEm: LocalDateTime? = null
) {

    @PrePersist
    fun antesDeInserir() {
        val agora = LocalDateTime.now()
        dataEmissao = agora
        criadoEm = agora
    }
}