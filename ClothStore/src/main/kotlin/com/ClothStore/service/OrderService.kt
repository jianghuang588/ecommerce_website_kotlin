package com.ClothStore.service

import com.ClothStore.domain.*


interface OrderService {
    fun createOrder(
        shoppingCart: ShoppingCart?,
        shippingAddress: ShippingAddress?,
        billingAddress: BillingAddress?,
        payment: Payment?,
        shippingMethod: String?,
        user: User?,
    ): Order?

    fun findOne(id: Long?): Order?
}