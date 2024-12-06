package com.ClothStore.domain.security

import com.ClothStore.domain.User
import java.util.*
import javax.persistence.*

@Entity
class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = 0

    var token: String? = null

    @OneToOne(targetEntity = User::class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    var user: User? = null

    var expiryDate: Date? = null

    constructor()

    constructor(token: String?, user: User?) : super() {
        this.token = token
        this.user = user
        this.expiryDate = calculateExpiryDate(expiration)
    }

    private fun calculateExpiryDate(expiryTimeInMinutes: Int): Date {
        val cal = Calendar.getInstance()
        cal.timeInMillis = Date().time
        cal.add(Calendar.MINUTE, expiryTimeInMinutes)
        return Date(cal.time.time)
    }

    fun updateToken(token: String?) {
        this.token = token
        this.expiryDate = calculateExpiryDate(expiration)
    }

    override fun toString(): String {
        return ("PasswordResetToken [id=" + id + ", token=" + token + ", user=" + user + ", expiryDate=" + expiryDate
                + "]")
    }


    companion object {
        const val expiration: Int = 60 * 24
    }
}