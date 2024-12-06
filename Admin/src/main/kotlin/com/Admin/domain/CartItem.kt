package com.Admin.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import java.math.BigDecimal
import javax.persistence.*


@Entity
class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var identity: Long? = null
    var quality: Int = 0
    var pretotal: BigDecimal? = null

    @OneToOne
    var shirt: Shirt? = null

    @OneToMany(mappedBy = "cartItem")
    @JsonIgnore
    var shirtToCartItemList: List<ShirtToCartItem>? = null


    @ManyToOne
    @JoinColumn(name = "shopping_cart_id")
    var shoppingCart: ShoppingCart? = null

    @ManyToOne
    @JoinColumn(name = "order_id")
    var order: Order? = null
}

