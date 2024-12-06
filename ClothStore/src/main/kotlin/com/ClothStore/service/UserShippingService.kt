package com.ClothStore.service

import com.ClothStore.domain.UserShipping



interface UserShippingService {
    fun findById(id: Long?): UserShipping?

    fun removeById(id: Long?)
}