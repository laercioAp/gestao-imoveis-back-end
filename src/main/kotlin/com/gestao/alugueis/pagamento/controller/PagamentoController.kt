
package com.gestao.alugueis.pagamento.controller

import com.gestao.alugueis.pagamento.dto.GerarPagamentosRequest
import com.gestao.alugueis.pagamento.dto.PagamentoRequest
import com.gestao.alugueis.pagamento.dto.PagamentoResponse
import com.gestao.alugueis.pagamento.service.PagamentoService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.LocalDate

@RestController
@RequestMapping("/api/pagamentos")
@CrossOrigin(origins = ["http://localhost:4200"])
class PagamentoController(
    private val pagamentoService: PagamentoService
) {

    @GetMapping
    fun listarTodos(): ResponseEntity<List<PagamentoResponse>> =
        ResponseEntity.ok(pagamentoService.listarTodos())

    @GetMapping("/{id}")
    fun buscarPorId(
        @PathVariable id: Long
    ): ResponseEntity<PagamentoResponse> =
        ResponseEntity.ok(pagamentoService.listarPorId(id))

    @GetMapping("/contrato/{contratoId}")
    fun listarPorContrato(
        @PathVariable contratoId: Long
    ): ResponseEntity<List<PagamentoResponse>> =
        ResponseEntity.ok(pagamentoService.listarPorContrato(contratoId))

    @GetMapping("/status/{status}")
    fun listarPorStatus(
        @PathVariable status: String
    ): ResponseEntity<List<PagamentoResponse>> =
        ResponseEntity.ok(pagamentoService.listarPorStatus(status))

    @GetMapping("/atrasados")
    fun listarAtrasados(): ResponseEntity<List<PagamentoResponse>> =
        ResponseEntity.ok(pagamentoService.listarAtrasados())

    @PostMapping
    fun cadastrar(
        @Valid @RequestBody request: PagamentoRequest
    ): ResponseEntity<PagamentoResponse> {
        val pagamento = pagamentoService.cadastrar(request)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(pagamento)
    }

    @PostMapping("/gerar-mensalidade")
    fun gerarPagamentosMensais(
        @Valid @RequestBody request: GerarPagamentosRequest
    ): ResponseEntity<Map<String, Any>> {

        val resultado = pagamentoService.gerarPagamentosMensais(request)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(resultado)
    }

    @PatchMapping("/{id}/registrar-pagamento")
    fun registrarPagamento(
        @PathVariable id: Long,
        @RequestBody request: RegistrarPagamentoRequest
    ): ResponseEntity<PagamentoResponse> {
        val pagamento = pagamentoService.registrarPagamento(
            id = id,
            valorPago = request.valorPago,
            dataPagamento = request.dataPagamento,
            formaPagamento = request.formaPagamento
        )

        return ResponseEntity.ok(pagamento)
    }

    @DeleteMapping("/{id}")
    fun excluir(
        @PathVariable id: Long
    ): ResponseEntity<Void> {
        pagamentoService.excluir(id)
        return ResponseEntity.noContent().build()
    }
}

data class RegistrarPagamentoRequest(
    val valorPago: BigDecimal,
    val dataPagamento: LocalDate,
    val formaPagamento: String
)