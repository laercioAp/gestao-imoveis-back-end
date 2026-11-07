
package com.gestao.alugueis.shared.exception

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException::class)
    fun tratarNaoEncontrado(
        ex: NoSuchElementException,
        request: HttpServletRequest
    ): ResponseEntity<ApiErrorResponse> {
        return criarResposta(
            HttpStatus.NOT_FOUND,
            ex.message ?: "Registro não encontrado.",
            request
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun tratarValidacao(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<ApiErrorResponse> {

        val mensagens = ex.bindingResult.allErrors
            .mapNotNull { erro ->
                (erro as? FieldError)?.defaultMessage
            }

        return criarResposta(
            HttpStatus.BAD_REQUEST,
            mensagens.joinToString("; "),
            request
        )
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun tratarViolacao(
        ex: ConstraintViolationException,
        request: HttpServletRequest
    ): ResponseEntity<ApiErrorResponse> {
        return criarResposta(
            HttpStatus.BAD_REQUEST,
            ex.message ?: "Dados inválidos.",
            request
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun tratarJsonInvalido(
        ex: HttpMessageNotReadableException,
        request: HttpServletRequest
    ): ResponseEntity<ApiErrorResponse> {
        return criarResposta(
            HttpStatus.BAD_REQUEST,
            "JSON inválido ou com formato de dados incorreto.",
            request
        )
    }

    @ExceptionHandler(Exception::class)
    fun tratarErroInterno(
        ex: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ApiErrorResponse> {
        return criarResposta(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Ocorreu um erro interno no servidor.",
            request
        )
    }

    private fun criarResposta(
        status: HttpStatus,
        mensagem: String,
        request: HttpServletRequest
    ): ResponseEntity<ApiErrorResponse> {

        val resposta = ApiErrorResponse(
            status = status.value(),
            error = status.reasonPhrase,
            message = mensagem,
            path = request.requestURI
        )

        return ResponseEntity.status(status).body(resposta)
    }


    @ExceptionHandler(IllegalArgumentException::class)
    fun tratarArgumentoInvalido(
        ex: IllegalArgumentException,
        request: HttpServletRequest
    ): ResponseEntity<ApiErrorResponse> {
        return criarResposta(
            HttpStatus.BAD_REQUEST,
            ex.message ?: "Dados inválidos.",
            request
        )
    }

}