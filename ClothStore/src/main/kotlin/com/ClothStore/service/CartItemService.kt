package com.ClothStore.service

import com.ClothStore.domain.*


interface CartItemService {
    fun findByShoppingCart(shoppingCart: ShoppingCart?): List<CartItem?>?
    fun updateCartItem(cartItem: CartItem?): CartItem?
    fun addShirtToCartItem(shirt: Shirt?, user: User?, qty: Int): CartItem?
    fun findById(id: Long?): CartItem?
    fun removeCartItem(cartItem: CartItem?)
    fun save(cartItem: Any): CartItem?
    fun findByOrder(order: Order?): List<CartItem?>?


}