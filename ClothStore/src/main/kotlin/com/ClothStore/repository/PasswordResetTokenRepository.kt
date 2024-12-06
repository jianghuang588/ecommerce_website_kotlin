package com.ClothStore.repository

import com.ClothStore.domain.User
import com.ClothStore.domain.security.PasswordResetToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.*
import java.util.stream.Stream


interface PasswordResetTokenRepository : JpaRepository<PasswordResetToken?, Long?> {
    fun findByToken(token: String?): PasswordResetToken?

    fun findByUser(user: User?): PasswordResetToken?

    fun findAllByExpiryDateLessThan(now: Date?): Stream<PasswordResetToken?>?

    @Modifying
    @Query("delete from PasswordResetToken t where t.expiryDate <= ?1")
    fun deleteAllExpiredSince(now: Date?)
}

