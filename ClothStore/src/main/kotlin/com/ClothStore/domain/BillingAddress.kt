package com.ClothStore.domain

import javax.persistence.*


@Entity
class BillingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var identifier: Long? = null
    var billingRecipientName: String? = null
    var billingStreetAddress1: String? = null
    var billingStreetAddress2: String? = null
    var billingCityName: String? = null
    var billingStateCode: String? = null
    var billingCountryName: String? = null
    var billingPostalCode: String? = null

    @OneToOne
    var purchaseRequest: Order? = null
}
