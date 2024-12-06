package com.ClothStore.service

import com.ClothStore.domain.ShoppingCart


interface ShoppingCartService {
    fun updateShoppingCart(shoppingCart: ShoppingCart?): ShoppingCart?

    fun clearShoppingCart(shoppingCart: ShoppingCart?)
}