package com.ClothStore.repository

import com.ClothStore.domain.security.Role
import org.springframework.data.repository.CrudRepository


interface RoleRepository : CrudRepository<Role?, Int?> {
    fun findByname(name: String?): Role?
}
