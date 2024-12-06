package com.ClothStore.service.impl

import com.ClothStore.domain.Payment
import com.ClothStore.domain.UserPayment
import com.ClothStore.service.PaymentService
import org.springframework.stereotype.Service


@Service
class PaymentServiceImpl : PaymentService {
    override fun setByUserPayment(userTransaction: UserPayment?, transaction: Payment?): Payment? {
        transaction!!.type = userTransaction!!.type
        transaction.holderName = userTransaction.holderName
        transaction.cardNumber = userTransaction.cardNumber
        transaction.expiryMonth = userTransaction.expiryMonth
        transaction.expiryYear = userTransaction.expiryYear
        transaction.cvc = userTransaction.cvc
        return transaction
    }
}