package com.ClothStore.service.impl

import com.ClothStore.domain.UserPayment
import com.ClothStore.repository.UserPaymentRepository
import com.ClothStore.service.UserPaymentService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class UserPaymentServiceImpl : UserPaymentService {
    @Autowired
    private val customerPaymentRepository: UserPaymentRepository? = null


    override fun findById(identittty: Long?): UserPayment? {
        return identittty?.let { customerPaymentRepository?.findById(it)?.orElse(null) }
    }

    override fun removeById(identittty: Long?) {
        identittty?.let { customerPaymentRepository?.deleteById(it) }
    }
}
