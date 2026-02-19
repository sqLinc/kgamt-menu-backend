package com.kgamt.menu.controller

import com.kgamt.menu.dto.CreateMenuRequest
import com.kgamt.menu.dto.MenuResponse
import com.kgamt.menu.service.MenuService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
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

    @GetMapping("/full")
    fun getFullMenu() : List<MenuResponse> {
        return menuService.getFullMenu()
    }

    @PostMapping
    fun saveMenu(
        @RequestBody request: CreateMenuRequest
    ) {
        menuService.createMenuByDate(request)
    }

    @PutMapping
    fun updateMenu(
        @RequestBody request: CreateMenuRequest
    ) {
        menuService.updateMenuByDate(request)
    }

    @DeleteMapping
    fun deleteMenu(
        @RequestParam date: String
    ) {
        menuService.deleteMenuByDate(LocalDate.parse(date))
    }

    @DeleteMapping("/deleteFull")
    fun deleteWeekMenu() {
        menuService.deleteWeekMenu()
    }

}