package com.ClothStore.repository

import com.ClothStore.domain.ShoppingCart
import org.springframework.data.repository.CrudRepository


interface ShoppingCartRepository : CrudRepository<ShoppingCart?, Long?>