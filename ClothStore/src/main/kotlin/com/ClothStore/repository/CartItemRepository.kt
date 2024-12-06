package com.ClothStore.repository

import com.ClothStore.domain.CartItem
import com.ClothStore.domain.Order
import com.ClothStore.domain.ShoppingCart
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional


@Transactional
interface CartItemRepository : CrudRepository<CartItem?, Long?> {
    fun findByShoppingCart(shoppingCart: ShoppingCart?): List<CartItem?>?

    fun findByOrder(order: Order?): List<CartItem?>?
}
