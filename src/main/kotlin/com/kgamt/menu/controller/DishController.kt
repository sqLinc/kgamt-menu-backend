package com.kgamt.menu.controller

import com.kgamt.menu.entity.Dish
import com.kgamt.menu.service.DishService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/dishes")
class DishController(
    private val dishService: DishService
) {

    @GetMapping
    fun getAll(): List<Dish> =
        dishService.getAll()

    @PostMapping
    fun create(
        @RequestBody dish: Dish
    ): Dish =
        dishService.save(dish)
}