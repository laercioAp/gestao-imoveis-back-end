
package com.gestao.alugueis.contrato.entity

import jakarta.persistence.*

@Entity
@Table(name = "contrato_clausulas")
class ContratoClausula(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contrato_id", nullable = false)
    var contrato: Contrato,

    @Column(nullable = false)
    var ordem: Int,

    @Column(nullable = false, length = 200)
    var titulo: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    var conteudo: String,

    @Column(nullable = false)
    var ativa: Boolean = true,

    @Column(nullable = false)
    var personalizada: Boolean = false
)
