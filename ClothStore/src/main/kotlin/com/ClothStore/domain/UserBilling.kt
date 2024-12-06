package com.ClothStore.domain

import javax.persistence.*


@Entity
class UserBilling {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
    var nameOfUserBilling: String? = null
    var streetOneUserBilling: String? = null
    var streetTwoUserBilling: String? = null
    var billingCityOfUser: String? = null
    var billingStateOfUser: String? = null
    var billingCountryForUser: String? = null
    var billingZipcodeForUser: String? = null

    @OneToOne(cascade = [CascadeType.ALL])
    var userPayment: UserPayment? = null
}
