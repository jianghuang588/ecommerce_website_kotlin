package com.Admin.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import java.math.BigDecimal
import javax.persistence.*


@Entity
class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var identity: Long? = null
    var total: BigDecimal? = null

    @OneToMany(mappedBy = "shoppingCart", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JsonIgnore
    var listItem: List<CartItem>? = null

    @OneToOne(cascade = [CascadeType.ALL])
    var user: User? = null
}
