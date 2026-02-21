package com.kgamt.menu.controller

import com.kgamt.menu.dto.CreateMenuRequest
import com.kgamt.menu.dto.MenuItemDto
import com.kgamt.menu.dto.MenuResponse
import com.kgamt.menu.entity.Dish
import com.kgamt.menu.entity.DishCategory
import com.kgamt.menu.entity.MenuDay
import com.kgamt.menu.entity.MenuItem
import com.kgamt.menu.repository.DishRepository
import com.kgamt.menu.repository.MenuDayRepository
import com.kgamt.menu.repository.MenuItemRepository
import org.springframework.cglib.core.Local
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate

@Controller
class AdminPageController(
    private val menuDayRepository: MenuDayRepository,
    private val dishRepository: DishRepository,
    private val menuItemRepository: MenuItemRepository
) {

    @GetMapping("/admin")
    fun adminPage(model: Model): String {
        val menuList: MutableList<MenuResponse> = mutableListOf()
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
                            category = it.dish.category.displayName
                        )
                    }
                menuList.add(MenuResponse(menuDay.date, menuDay.weekDay, items))

            }
        model.addAttribute("menuItems", menuList)

        val dishes = dishRepository.findAll()
        model.addAttribute("dishes", dishes)

        val dishesByCategory: Map<DishCategory, List<Dish>> = dishRepository.findAll()
            .groupBy { it.category }
        model.addAttribute("dishCat", dishesByCategory)


        return "admin"
    }


    @GetMapping("/login")
    fun loginPage(): String {
        return "login"
    }

    @GetMapping("/admin/create_dish")
    fun createDishForm(model: Model): String {
        model.addAttribute("category", DishCategory.entries.toTypedArray())
        return "create-dish"
    }

    @PostMapping("/admin/create_dish")
    fun createDish(
        @RequestParam name: String,
        @RequestParam price: Int,
        @RequestParam quantity: Int,
        @RequestParam kcal: Int,
        @RequestParam description: String,
        @RequestParam category: DishCategory
    ) : String {
        dishRepository.save(Dish(name = name, price = price, quantity = quantity, kcal = kcal, desc = description, category = category))

        return "redirect:/admin"
    }

    @GetMapping("/admin/create")
    fun createMenuForm(model: Model): String {
        model.addAttribute("dishes", dishRepository.findAll())
        return "create-menu"
    }

    @PostMapping("/admin/create")
    fun createMenu(
        @RequestParam date: String,
        @RequestParam weekDay: String,
        @RequestParam(required = false) dishIds: List<Long>?
    ): String {
        if (menuDayRepository.findByDate(LocalDate.parse(date)) != null) {
            return "redirect:/admin?error=exists"
        }

        val menuDay = menuDayRepository.save(MenuDay(date = LocalDate.parse(date), weekDay = weekDay))
        dishIds?.forEach { id ->
            val dish = dishRepository.findById(id).orElse(null)
            if (dish != null) {
                menuItemRepository.save(MenuItem(menuDay = menuDay, dish = dish))
            }
        }

        return "redirect:/admin"

    }

    @DeleteMapping("/admin/delete/week")
    fun deleteWeek() : String {
        menuItemRepository.deleteAll()
        menuDayRepository.deleteAll()

        return "redirect:/admin"
    }

    @DeleteMapping("/admin/delete/dish")
    fun deleteDish(
        @RequestParam id: Long
    ): String{
        dishRepository.deleteById(id)
        return "redirect:/admin"
    }

    @DeleteMapping("/admin/delete/day")
    fun deleteDay(
        @RequestParam date: String
    ): String {
        val menuDay = menuDayRepository.findByDate(LocalDate.parse(date))
        val items = menuItemRepository.findAllByMenuDay(menuDay!!)

        menuItemRepository.deleteAll(items)
        menuDayRepository.delete(menuDay)

        return "redirect:/admin"
    }

    @GetMapping("/admin/editDish/{id}")
    fun editDishPage(
        @PathVariable id: Long,
        model: Model
    ): String {
        val dish = dishRepository.findById(id).orElseThrow {
            RuntimeException("Блюдо не найдено")
        }
        model.addAttribute("dish", dish)
        model.addAttribute("category", DishCategory.entries.toTypedArray())



        return "edit-dish"
    }

    @PostMapping("/admin/editDish/{id}")
    fun editDish(
        @PathVariable id: Long,
        @RequestParam name: String,
        @RequestParam price: Int,
        @RequestParam quantity: Int,
        @RequestParam kcal: Int,
        @RequestParam description: String,
        @RequestParam category: DishCategory
    ): String {
        dishRepository.deleteById(id)
        dishRepository.save(Dish(name = name, price = price, quantity = quantity, kcal = kcal, desc = description, category = category))

        return "redirect:/admin"
    }

    @GetMapping("/admin/edit/{date}")
    fun editMenuPage(
        @PathVariable date: LocalDate,
        model: Model
    ): String {

        val menuDay = menuDayRepository.findByDate(date) ?: return "redirect:/admin"
        model.addAttribute("menu", menuDay)

        // Все блюда из базы
        val allDishes = dishRepository.findAll()
        model.addAttribute("dishes", allDishes)

        // ID блюд, которые уже есть в меню на этот день
        val selectedDishIds = menuItemRepository.findAllByMenuDay(menuDay)
            .map { it.dish.id }
        model.addAttribute("selectedDishIds", selectedDishIds)

        return "edit-menu"
    }


    @PostMapping("/admin/edit/{date}")
    fun updateMenuDay(
        @PathVariable date: LocalDate,
        @RequestParam(required = false) dishIds: List<Long>?
    ): String {
        val menuDay = menuDayRepository.findByDate(date) ?: return "redirect:/admin"


        val items = menuItemRepository.findAllByMenuDay(menuDay)
        menuItemRepository.deleteAll(items)


        // Добавляем выбранные новые
        dishIds?.forEach { id ->
            val dish = dishRepository.findById(id).orElse(null)
            if (dish != null) {
                menuItemRepository.save(MenuItem(menuDay = menuDay, dish = dish))
            }
        }

        return "redirect:/admin"
    }

    @GetMapping("/dish/{id}")
    fun descriptionPage(
        @PathVariable id: Long,
        model: Model
    ) : String {
        val dish = dishRepository.findById(id).orElseThrow {
            RuntimeException("Блюдо не найдено")
        }
        model.addAttribute("dish", dish)

        return "description-page"
    }


}