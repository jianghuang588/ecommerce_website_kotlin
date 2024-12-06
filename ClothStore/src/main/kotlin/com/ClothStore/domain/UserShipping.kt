package com.ClothStore.domain

import javax.persistence.*


@Entity
class UserShipping {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    var userShippingName: String? = null
    var userShippingStreet1: String? = null
    var userShippingStreet2: String? = null
    var userShippingCity: String? = null
    var userShippingState: String? = null
    var userShippingCountry: String? = null
    var userShippingZipcode: String? = null
    var isUserShippingDefault: Boolean = false


    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User? = null
}