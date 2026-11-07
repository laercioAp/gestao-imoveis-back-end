
package com.gestao.alugueis.dashboard.controller

import com.gestao.alugueis.dashboard.dto.DashboardResponse
import com.gestao.alugueis.dashboard.service.DashboardService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = ["http://localhost:4200"])
class DashboardController(
    private val dashboardService: DashboardService
) {

    @GetMapping("/resumo")
    fun obterResumo(): ResponseEntity<DashboardResponse> =
        ResponseEntity.ok(dashboardService.obterResumo())
}