package com.kgamt.menu.service

import com.kgamt.menu.dto.CreateMenuRequest
import com.kgamt.menu.dto.MenuItemDto
import com.kgamt.menu.dto.MenuResponse
import com.kgamt.menu.entity.MenuDay
import com.kgamt.menu.entity.MenuItem
import com.kgamt.menu.repository.DishRepository
import com.kgamt.menu.repository.MenuDayRepository
import com.kgamt.menu.repository.MenuItemRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.Collections.emptyList

@Service
class MenuService(
    private val menuDayRepository: MenuDayRepository,
    private val menuItemRepository: MenuItemRepository,
    private val dishRepository: DishRepository,
) {

    fun getFullMenu(): List<MenuResponse> {
        val menuResponseList: MutableList<MenuResponse> = mutableListOf()
        val menuDays = menuDayRepository.findAll()

        menuDays
            .forEach { menuDay ->
                val items = menuItemRepository.findAllByMenuDay(menuDay)
                    .map {
                        MenuItemDto(
                            id = it.dish.id,
                            name = it.dish.name,
                            price = it.dish.price,
                            quantity = it.dish.quantity,
                            kcal = it.dish.kcal,
                            desc = it.dish.desc,
                            category = it.dish.category.toString()
                        )
                    }
                menuResponseList.add(MenuResponse(menuDay.date, menuDay.weekDay, items))

        }
        return menuResponseList

    }

    fun getMenuByDate(date: LocalDate): MenuResponse {
        val menuDay = menuDayRepository.findByDate(date)
            ?: throw RuntimeException("Menu not found")

        val items = menuItemRepository.findAllByMenuDay(menuDay)
            .map {
                MenuItemDto(
                    id = it.dish.id,
                    name = it.dish.name,
                    price = it.dish.price,
                    quantity = it.dish.quantity,
                    kcal = it.dish.kcal,
                    desc = it.dish.desc,
                    category = it.dish.category.toString()
                )
            }
        return MenuResponse(menuDay.date, menuDay.weekDay, items)
    }

    fun createMenuByDate(request: CreateMenuRequest){

        val menuDay = menuDayRepository.save(MenuDay(date = request.date, weekDay = request.weekDay))
        val dishes = dishRepository.findAllById(request.dishIds)

        dishes.forEach { dish ->
            menuItemRepository.save(MenuItem(menuDay = menuDay, dish = dish))
        }

    }

    @Transactional
    fun updateMenuByDate(request: CreateMenuRequest){
        val menuDay = menuDayRepository.findByDate(request.date)
            ?: throw RuntimeException("Menu not found")

        val oldItems = menuItemRepository.findAllByMenuDay(menuDay)
        menuItemRepository.deleteAll(oldItems)

        val items = dishRepository.findAllById(request.dishIds)

        items.forEach {  dish ->
            menuItemRepository.save(MenuItem(menuDay = menuDay, dish = dish))
        }
    }

    @Transactional
    fun deleteMenuByDate(date: LocalDate){
        val menuDay = menuDayRepository.findByDate(date)
            ?: throw RuntimeException("Menu not found")

        val items = menuItemRepository.findAllByMenuDay(menuDay)
        menuItemRepository.deleteAll(items)

        menuDayRepository.delete(menuDay)
    }

    @Transactional
    fun deleteWeekMenu() {
        menuItemRepository.deleteAll()
        menuDayRepository.deleteAll()
    }

}