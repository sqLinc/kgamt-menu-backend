package com.kgamt.menu.controller

import com.kgamt.menu.dto.MenuResponse
import com.kgamt.menu.service.MenuService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/api/menu")
class MenuController(
    private val menuService: MenuService
) {

    @GetMapping
    fun getMenu(
        @RequestParam date: String
    ): MenuResponse {
        return menuService.getMenuByDate(LocalDate.parse(date))
    }

}