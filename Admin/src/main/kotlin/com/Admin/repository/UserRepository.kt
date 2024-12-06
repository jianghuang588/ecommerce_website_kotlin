package com.Admin.repository

import com.Admin.domain.User
import org.springframework.data.repository.CrudRepository

// provide method for entiry user
// create, read, update, delete
interface UserRepository : CrudRepository<User?, Long?> {

    fun findByUsername(username: String?): User?

    fun findByEmail(email: String?): User?

    override fun findAll(): List<User>


}
