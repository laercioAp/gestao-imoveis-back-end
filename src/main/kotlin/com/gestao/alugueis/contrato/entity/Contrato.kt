
package com.gestao.alugueis.contrato.entity

import com.gestao.alugueis.imovel.entity.Imovel
import com.gestao.alugueis.inquilino.entity.Inquilino
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "contratos")
class Contrato(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "imovel_id", nullable = false)
    var imovel: Imovel,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inquilino_id", nullable = false)
    var inquilino: Inquilino,

    @Column(nullable = false, length = 100)
    var finalidade: String,

    @Column(name = "valor_aluguel", nullable = false, precision = 12, scale = 2)
    var valorAluguel: BigDecimal,

    @Column(name = "dia_vencimento", nullable = false)
    var diaVencimento: Int,

    @Column(name = "data_inicio", nullable = false)
    var dataInicio: LocalDate,

    @Column(name = "data_fim")
    var dataFim: LocalDate? = null,

    @Column(name = "indice_reajuste", length = 50)
    var indiceReajuste: String? = null,

    @Column(name = "valor_caucao", precision = 12, scale = 2)
    var valorCaucao: BigDecimal? = null,

    @Column(columnDefinition = "TEXT")
    var observacoes: String? = null,

    @Column(name = "locador_nome", length = 200)
    var locadorNome: String? = null,

    @Column(name = "locador_documento", length = 30)
    var locadorDocumento: String? = null,

    @Column(name = "locador_endereco", columnDefinition = "TEXT")
    var locadorEndereco: String? = null,

    @Column(nullable = false, length = 20)
    var status: String = "RASCUNHO",

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