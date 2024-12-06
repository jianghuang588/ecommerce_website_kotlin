package com.Admin.service

import com.Admin.domain.User
import com.Admin.domain.security.PasswordResetToken
import com.Admin.domain.security.UserRole


interface UserService {
    fun getPasswordResetToken(token: String?): PasswordResetToken?

    fun createPasswordResetTokenForUser(user: User?, token: String?)

    fun findByUsername(username: String?): User?

    fun findByEmail(email: String?): User?

    @Throws(Exception::class)
    fun createUser(user: User?, userRoles: Set<UserRole?>?): User?

    fun save(user: User?): User?

    fun getAllUsers(): List<User?>?

    fun lockUserById(id: Long?)

    fun unlockUserById(id: Long?)

    fun lockUsersByIds(userIds: List<Long?>?)

    fun unlockUsersByIds(userIds: List<Long?>?)

}
