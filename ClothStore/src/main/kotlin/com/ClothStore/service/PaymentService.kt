package com.ClothStore.service

import com.ClothStore.domain.Payment
import com.ClothStore.domain.UserPayment


interface PaymentService {
    fun setByUserPayment(userPayment: UserPayment?, payment: Payment?): Payment?
}
