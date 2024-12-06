package com.ClothStore.repository

import com.ClothStore.domain.UserShipping
import org.springframework.data.repository.CrudRepository


interface UserShippingRepository : CrudRepository<UserShipping?, Long?>
