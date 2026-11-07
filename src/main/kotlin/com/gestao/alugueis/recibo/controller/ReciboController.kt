
package com.gestao.alugueis.recibo.controller

import com.gestao.alugueis.recibo.service.ReciboPdfService
import com.gestao.alugueis.recibo.service.ReciboService
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/recibos")
@CrossOrigin(origins = ["http://localhost:4200"])
class ReciboController(
    private val reciboService: ReciboService,
    private val reciboPdfService: ReciboPdfService
) {

    @PostMapping("/pagamento/{pagamentoId}")
    fun emitir(
        @PathVariable pagamentoId: Long
    ): ResponseEntity<Map<String, Any>> {

        val recibo = reciboService.emitir(pagamentoId)

        return ResponseEntity.ok(
            mapOf(
                "id" to recibo.id!!,
                "pagamentoId" to recibo.pagamento.id!!,
                "numeroRecibo" to recibo.numeroRecibo,
                "dataEmissao" to recibo.dataEmissao!!
            )
        )
    }

    @GetMapping("/pagamento/{pagamentoId}/pdf")
    fun baixarPdf(
        @PathVariable pagamentoId: Long
    ): ResponseEntity<ByteArray> {

        val recibo = reciboService.buscarPorPagamento(pagamentoId)
        val pdf = reciboPdfService.gerar(recibo)

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_PDF)
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"${recibo.numeroRecibo}.pdf\""
            )
            .body(pdf)
    }

    @GetMapping("/numero/{numeroRecibo}")
    fun buscarPorNumero(
        @PathVariable numeroRecibo: String
    ): ResponseEntity<Map<String, Any>> {

        val recibo = reciboService.buscarPorNumero(numeroRecibo)

        return ResponseEntity.ok(
            mapOf(
                "id" to recibo.id!!,
                "pagamentoId" to recibo.pagamento.id!!,
                "numeroRecibo" to recibo.numeroRecibo,
                "dataEmissao" to recibo.dataEmissao!!
            )
        )
    }
}