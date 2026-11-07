
package com.gestao.alugueis.config

import com.gestao.alugueis.usuario.repository.UsuarioRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val usuarioRepository: UsuarioRepository
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader("Authorization")

        if (authorizationHeader == null ||
            !authorizationHeader.startsWith("Bearer ")
        ) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authorizationHeader.substring(7)

        try {
            val email = jwtService.extrairEmail(token)

            if (SecurityContextHolder.getContext().authentication == null) {

                val usuario = usuarioRepository.findByEmail(email)

                if (usuario != null &&
                    usuario.ativo &&
                    jwtService.tokenValido(token, usuario.email)
                ) {
                    val userDetails = User.withUsername(usuario.email)
                        .password(usuario.senha)
                        .authorities("ROLE_USER")
                        .build()

                    val authentication =
                        UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.authorities
                        )

                    authentication.details =
                        WebAuthenticationDetailsSource()
                            .buildDetails(request)

                    SecurityContextHolder.getContext().authentication =
                        authentication
                }
            }
        } catch (ex: Exception) {
            SecurityContextHolder.clearContext()
        }

        filterChain.doFilter(request, response)
    }
}