
package com.gestao.alugueis.pagamento.entity

import com.gestao.alugueis.contrato.entity.Contrato
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(
    name = "pagamentos",
    uniqueConstraints = [
        UniqueConstraint(
            name = "uk_pagamentos_contrato_competencia",
            columnNames = ["contrato_id", "competencia"]
        )
    ]
)
class Pagamento(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrato_id", nullable = false)
    var contrato: Contrato,

    @Column(nullable = false)
    var competencia: LocalDate,

    @Column(name = "valor_previsto", nullable = false, precision = 12, scale = 2)
    var valorPrevisto: BigDecimal,

    @Column(name = "valor_pago", precision = 12, scale = 2)
    var valorPago: BigDecimal? = null,

    @Column(name = "data_vencimento", nullable = false)
    var dataVencimento: LocalDate,

    @Column(name = "data_pagamento")
    var dataPagamento: LocalDate? = null,

    @Column(name = "forma_pagamento", length = 30)
    var formaPagamento: String? = null,

    @Column(nullable = false, length = 20)
    var status: String = "PENDENTE",

    @Column(columnDefinition = "TEXT")
    var observacoes: String? = null,

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