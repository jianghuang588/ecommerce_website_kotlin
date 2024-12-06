package com.ClothStore.domain

import javax.persistence.*


@Entity
class ShirtToCartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    @ManyToOne
    @JoinColumn(name = "shirt_id")
    var shirt: Shirt? = null

    @ManyToOne
    @JoinColumn(name = "cart_item_id")
    var cartItem: CartItem? = null
}

