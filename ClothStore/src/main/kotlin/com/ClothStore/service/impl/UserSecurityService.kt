package com.ClothStore.service.impl

import com.ClothStore.domain.User
import com.ClothStore.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

// make the class service layer so
// springboot can provide the security method
@Service
// UserDetailsService retrive user information
class UserSecurityService : UserDetailsService {

    // pass UserRepository method to the object
    // customerRepository object contain ability to retrive username from mysql
    @Autowired
    private val customerRepository: UserRepository? = null

    // customerRepository provide the ability to search for username
    // below code check if the user type in username do exit
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String?): UserDetails {
        val client: User =
            customerRepository!!.findByUsername(username) ?: throw UsernameNotFoundException("Client not found")

        return client
    }
}
