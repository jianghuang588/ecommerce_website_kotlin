package com.ClothStore.service.impl

import com.ClothStore.domain.ShippingAddress
import com.ClothStore.domain.UserShipping
import com.ClothStore.service.ShippingAddressService
import org.springframework.stereotype.Service


@Service
class ShippingAddressServiceImpl : ShippingAddressService {
    override fun setByUserShipping(
        deliveryInfo: UserShipping?,
        addressForShipping: ShippingAddress?,
    ): ShippingAddress? {
        addressForShipping!!.nameOfShippingAddress = deliveryInfo!!.userShippingName
        addressForShipping.streetOneOfShippingAddress = deliveryInfo.userShippingStreet1
        addressForShipping.streetTwoOfShippingAddress = deliveryInfo.userShippingStreet2
        addressForShipping.cityOfShippingAddress = deliveryInfo.userShippingCity
        addressForShipping.stateOfShippingAddress = deliveryInfo.userShippingState
        addressForShipping.countryOfShippingAddress = deliveryInfo.userShippingCountry
        addressForShipping.zipcodeOfShippingAddress = deliveryInfo.userShippingZipcode

        return addressForShipping
    }
}
