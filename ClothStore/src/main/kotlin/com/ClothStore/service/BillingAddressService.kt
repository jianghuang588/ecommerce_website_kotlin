package com.ClothStore.service

import com.ClothStore.domain.BillingAddress
import com.ClothStore.domain.UserBilling


interface BillingAddressService {
    fun setByUserBilling(userBilling: UserBilling?, billingAddress: BillingAddress?): BillingAddress?
}

