package com.Admin.service

import com.Admin.domain.Shirt


interface ShirtService {
    fun save(shirt: Shirt?): Shirt?
    fun findAll(): List<Shirt?>?
    fun findOne(id: Long?): Shirt?
    fun removeOne(id: Long?)


}