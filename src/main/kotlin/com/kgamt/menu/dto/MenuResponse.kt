package com.kgamt.menu.dto

import com.kgamt.menu.entity.DishCategory
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.time.LocalDate

data class MenuItemDto(
    val id: Long,
    val name: String,
    val price: Int,
    val quantity: Int,
    val kcal: Int,
    val desc: String,
    val category: String
)

data class MenuResponse(
    val date: LocalDate,
    val weekDay: String,
    val items: List<MenuItemDto>,
)


