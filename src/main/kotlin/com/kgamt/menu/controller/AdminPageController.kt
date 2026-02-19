package com.kgamt.menu.controller

import com.kgamt.menu.repository.MenuDayRepository
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class AdminPageController(
    private val menuDayRepository: MenuDayRepository
) {

    @GetMapping("/admin")
    fun adminPage(model: Model): String {
        model.addAttribute("menuDays", menuDayRepository.findAll())
        return "admin"
    }

    @GetMapping("/login")
    fun loginPage(): String {
        return "login"
    }
}