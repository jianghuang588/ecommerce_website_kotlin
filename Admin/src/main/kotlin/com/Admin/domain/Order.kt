package com.Admin.domain

import java.math.BigDecimal
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "user_order")
class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var identifyer: Long? = null
    var dateOfOrder: Date? = null
    var dateOfShipping: Date? = null
    var methodOfShipping: String? = null
    var statusOfOrder: String? = null
    var totalOfOrder: BigDecimal = BigDecimal.ZERO

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL])
    var cartItemList: List<CartItem>? = null

    @OneToOne(cascade = [CascadeType.ALL])
    var shippingAddress: ShippingAddress? = null

    @OneToOne(cascade = [CascadeType.ALL])
    var billingAddress: BillingAddress? = null

    @OneToOne(cascade = [CascadeType.ALL])
    var payment: Payment? = null

    @ManyToOne
    var user: User? = null
}