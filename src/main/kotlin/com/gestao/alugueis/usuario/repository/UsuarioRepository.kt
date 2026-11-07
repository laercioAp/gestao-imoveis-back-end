
package com.gestao.alugueis.usuario.repository

import com.gestao.alugueis.usuario.entity.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UsuarioRepository : JpaRepository<Usuario, Long> {

    fun findByEmail(email: String): Usuario?

    fun existsByEmail(email: String): Boolean

}