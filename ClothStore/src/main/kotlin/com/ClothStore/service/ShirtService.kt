package com.ClothStore.service

import com.ClothStore.domain.Shirt


interface ShirtService {
    fun findAll(): List<Shirt?>?

    fun findOne(id: Long?): Shirt?

    fun findByCategory(category: String?): List<Shirt?>?

    fun blurrySearch(title: String?): List<Shirt?>?

}