package com.kgamt.menu.dto

import java.time.LocalDate

data class CreateMenuRequest(
    val date: LocalDate,
    val weekDay: String,
    val dishIds: List<Long>
)
