package com.kgamt.menu.service

import com.kgamt.menu.dto.MenuItemDto
import com.kgamt.menu.dto.MenuResponse
import com.kgamt.menu.repository.MenuDayRepository
import com.kgamt.menu.repository.MenuItemRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class MenuService(
    private val menuDayRepository: MenuDayRepository,
    private val menuItemRepository: MenuItemRepository
) {

    fun getMenuByDate(date: LocalDate): MenuResponse {
        val menuDay = menuDayRepository.findByDate(date)
            ?: throw RuntimeException("Menu not found")

        val items = menuItemRepository.findALlByMenuDay(menuDay)
            .map {
                MenuItemDto(
                    id = it.dish.id,
                    name = it.dish.name,
                    price = it.dish.price
                )
            }
        return MenuResponse(menuDay.date, items)
    }

}