package com.ClothStore.domain

import javax.persistence.*


@Entity
class ShippingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var identity: Long? = null
    var nameOfShippingAddress: String? = null
    var streetOneOfShippingAddress: String? = null
    var streetTwoOfShippingAddress: String? = null
    var cityOfShippingAddress: String? = null
    var stateOfShippingAddress: String? = null
    var countryOfShippingAddress: String? = null
    var zipcodeOfShippingAddress: String? = null


    @OneToOne
    var order: Order? = null
}
