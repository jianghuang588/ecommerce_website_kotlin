package com.ClothStore.service

import com.ClothStore.domain.UserPayment


interface UserPaymentService {
    fun findById(id: Long?): UserPayment?
    fun removeById(id: Long?)
}