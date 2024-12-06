package com.Admin.utility

import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.security.SecureRandom
import kotlin.random.Random

// make the class the returnable value
@Component
object SecurityUtility {
    private const val SALT = "salt"

    // @Bean help to create only 1 instance
    // password that will perform 2¹² (4,096)  iteration
    //
    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder(12, SecureRandom(SALT.toByteArray()))
    }

    // @Bean help to genearte one unique passowrd
    @Bean
    fun randomPassword(): String {
        val SALT_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456789"
        val saltToken = StringBuilder()
        val random = Random

        while (saltToken.length < 18) {
            val index = random.nextInt(SALT_ALPHABET.length)
            saltToken.append(SALT_ALPHABET[index])
        }

        val saltString = saltToken.toString()
        return saltString
    }
}
