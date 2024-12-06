package com.ClothStore.service.impl

import com.ClothStore.domain.UserShipping
import com.ClothStore.repository.UserShippingRepository
import com.ClothStore.service.UserShippingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class UserShippingServiceImpl : UserShippingService {
    @Autowired
    private val userShippingDataRepository: UserShippingRepository? = null


    override fun findById(identity: Long?): UserShipping? {
        return identity?.let { userShippingDataRepository?.findById(it)?.orElse(null) }
    }


    override fun removeById(identity: Long?) {
        identity?.let { userShippingDataRepository?.deleteById(it) }
    }


}
