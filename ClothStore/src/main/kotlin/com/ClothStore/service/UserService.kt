package com.ClothStore.service

import com.ClothStore.domain.User
import com.ClothStore.domain.UserBilling
import com.ClothStore.domain.UserPayment
import com.ClothStore.domain.UserShipping
import com.ClothStore.domain.security.PasswordResetToken
import com.ClothStore.domain.security.UserRole


interface UserService {
    fun getPasswordResetToken(token: String?): PasswordResetToken?

    fun createPasswordResetTokenForUser(user: User?, token: String?)

    fun findByUsername(username: String?): User?

    fun findByEmail(email: String?): User?

    @Throws(Exception::class)
    fun createUser(user: User?, userRoles: Set<UserRole?>?): User?

    fun save(user: User?): User?

    fun updateUserBilling(userBilling: UserBilling?, userPayment: UserPayment?, user: User?)

    fun setUserDefaultPayment(userPaymentId: Long?, user: User?)

    fun updateUserShipping(userShipping: UserShipping?, user: User?)

    fun setUserDefaultShipping(userShippingId: Long?, user: User?)

    fun findById(id: Long?): User?


}
