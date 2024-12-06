package com.Admin.domain

import javax.persistence.*


@Entity
class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    var type: String? = null
    var cardName: String? = null
    var cardNumber: String? = null
    var expiryMonth: Int = 0
    var expiryYear: Int = 0
    var cvc: Int = 0
    var holderName: String? = null

    @OneToOne
    var order: Order? = null

    @OneToOne(cascade = [CascadeType.ALL], mappedBy = "userPayment")
    var userBilling: UserBilling? = null
}


