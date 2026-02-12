package com.kgamt.menu.service

import com.kgamt.menu.entity.Dish
import com.kgamt.menu.repository.DishRepository
import org.springframework.stereotype.Service

@Service
class DishService(
    private val dishRepository: DishRepository
) {

    fun getAll(): List<Dish> =
        dishRepository.findAll()

    fun save(dish: Dish): Dish =
        dishRepository.save(dish)
}