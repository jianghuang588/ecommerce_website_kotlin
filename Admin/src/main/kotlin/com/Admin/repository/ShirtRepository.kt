package com.Admin.repository

import com.Admin.domain.Shirt
import org.springframework.data.repository.CrudRepository


interface ShirtRepository : CrudRepository<Shirt?, Long?> {
    //fun findByCategory(category: String?): List<Shirt?>?

    //fun findByTitleContaining(title: String?): List<Shirt?>?
}
