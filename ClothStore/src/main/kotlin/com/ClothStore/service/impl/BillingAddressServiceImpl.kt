package com.ClothStore.service.impl

import com.ClothStore.domain.BillingAddress
import com.ClothStore.domain.UserBilling
import com.ClothStore.service.BillingAddressService
import org.springframework.stereotype.Service


@Service
class BillingAddressServiceImpl : BillingAddressService {
    override fun setByUserBilling(userBilling: UserBilling?, billingAddress: BillingAddress?): BillingAddress? {
        billingAddress!!.billingRecipientName = userBilling!!.nameOfUserBilling
        billingAddress.billingStreetAddress1 = userBilling.streetOneUserBilling
        billingAddress.billingStreetAddress2 = userBilling.streetTwoUserBilling
        billingAddress.billingCityName = userBilling.billingCityOfUser
        billingAddress.billingStateCode = userBilling.billingStateOfUser
        billingAddress.billingCountryName = userBilling.billingCountryForUser
        billingAddress.billingPostalCode = userBilling.billingZipcodeForUser

        return billingAddress
    }
}
