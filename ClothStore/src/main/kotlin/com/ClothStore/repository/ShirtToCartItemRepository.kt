package com.ClothStore.repository

import com.ClothStore.domain.CartItem
import com.ClothStore.domain.ShirtToCartItem
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional


@Transactional
interface ShirtToCartItemRepository : CrudRepository<ShirtToCartItem?, Long?> {
    fun deleteByCartItem(cartItem: CartItem?)
}