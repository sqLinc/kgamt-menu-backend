package com.kgamt.menu.repository

import com.kgamt.menu.entity.MenuDay
import com.kgamt.menu.entity.MenuItem
import org.springframework.data.jpa.repository.JpaRepository


interface MenuItemRepository : JpaRepository<MenuItem, Long> {
    fun findALlByMenuDay(menuDay: MenuDay): List<MenuItem>
}