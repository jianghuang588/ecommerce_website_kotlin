package com.ClothStore.repository

import com.ClothStore.domain.UserPayment
import org.springframework.data.repository.CrudRepository


interface UserPaymentRepository : CrudRepository<UserPayment?, Long?>