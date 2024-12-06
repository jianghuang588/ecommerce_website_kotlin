package com.ClothStore.repository

import com.ClothStore.domain.Order
import org.springframework.data.repository.CrudRepository


interface OrderRepository : CrudRepository<Order?, Long?>