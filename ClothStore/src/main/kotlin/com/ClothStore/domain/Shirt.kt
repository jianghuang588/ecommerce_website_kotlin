package com.ClothStore.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.web.multipart.MultipartFile
import javax.persistence.*


@Entity
class Shirt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    var title: String? = null
    var author: String? = null
    var publisher: String? = null
    var publicationDate: String? = null
    var language: String? = null
    var category: String? = null
    var numberOfPages: Int = 0
    var format: String? = null
    var isbn: Int = 0
    var shippingWeight: Double = 0.0
    var listPrice: Double = 0.0
    var ourPrice: Double = 0.0

    var isActive: Boolean = true

    @Column(columnDefinition = "text")
    var description: String? = null
    var inStockNumber: Int = 0

    @Transient
    var shirtImage: MultipartFile? = null

    @OneToMany(mappedBy = "shirt")
    @JsonIgnore
    private val shirtToCartItemList: List<ShirtToCartItem>? = null

}