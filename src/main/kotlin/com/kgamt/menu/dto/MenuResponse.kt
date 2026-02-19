package com.kgamt.menu.dto

import java.time.LocalDate

data class MenuItemDto(
    val id: Long,
    val name: String,
    val price: Double,
)

data class MenuResponse(
    val date: LocalDate,
    val weekDay: String,
    val items: List<MenuItemDto>,
)


