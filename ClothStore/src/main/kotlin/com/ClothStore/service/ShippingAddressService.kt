package com.ClothStore.service

import com.ClothStore.domain.ShippingAddress
import com.ClothStore.domain.UserShipping


interface ShippingAddressService {
    fun setByUserShipping(userShipping: UserShipping?, shippingAddress: ShippingAddress?): ShippingAddress?
}
