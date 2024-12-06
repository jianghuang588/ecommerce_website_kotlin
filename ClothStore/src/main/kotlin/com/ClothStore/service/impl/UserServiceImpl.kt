package com.ClothStore.service.impl

import com.ClothStore.domain.*
import com.ClothStore.domain.security.PasswordResetToken
import com.ClothStore.domain.security.UserRole
import com.ClothStore.repository.*
import com.ClothStore.service.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class UserServiceImpl : UserService {

    private val LOG: Logger = LoggerFactory.getLogger(UserService::class.java)

    @Autowired
    private val resetTokenRepository: PasswordResetTokenRepository? = null

    @Autowired
    private val userDirectory: UserRepository? = null

    @Autowired
    private val userRoleRepository: RoleRepository? = null

    @Autowired
    private val customerPaymentRepository: UserPaymentRepository? = null

    @Autowired
    private val userShippingData: UserShippingRepository? = null

    @Autowired
    override fun getPasswordResetToken(key: String?): PasswordResetToken? {
        return resetTokenRepository!!.findByToken(key)
    }

    override fun createPasswordResetTokenForUser(client: User?, key: String?) {
        val myKey = PasswordResetToken(key, client)
        resetTokenRepository!!.save(myKey)
    }

    override fun findByUsername(identity: String?): User? {
        return userDirectory?.findByUsername(identity)
    }


    override fun findByEmail(mail: String?): User? {
        return userDirectory?.findByEmail(mail)
    }

    override fun findById(identity: Long?): User {
        return userDirectory!!.findById(identity!!).orElse(null)!!
    }

    override fun createUser(client: User?, accountRoles: Set<UserRole?>?): User? {
        var internalUser = userDirectory!!.findByUsername(client?.getUsername())

        if (internalUser != null) {
            LOG.info("Client {} already exists. No action is needed.", client?.getUsername())
        } else {
            for (ur in accountRoles!!) {
                userRoleRepository!!.save(ur?.role!!)            }

            //client.getUserRoles().addAll(accountRoles)

            client!!.userRoles = client.userRoles + (accountRoles?.filterNotNull() ?: emptySet())



            val cart: ShoppingCart = ShoppingCart()
            //cart.setUser(client)
            cart.user = client
            //client.setShoppingCart(cart)
            client.shoppingCart = cart
            //client.setUserShippingList(ArrayList<UserShipping>())

            client.userShippingList = ArrayList<UserShipping>()

            //client.setUserPaymentList(ArrayList<UserPayment>())
            client.userPaymentList = ArrayList<UserPayment>()

            internalUser = userDirectory.save(client)
        }
        return internalUser
    }

    override fun save(client: User?): User {
        return userDirectory!!.save(client!!)
    }

    override fun updateUserBilling(userBilling: UserBilling?, userPayment: UserPayment?, user: User?) {
        userPayment?.user = user
        userPayment?.userBilling = userBilling
        userPayment?.isDefaultPayment = true
        userBilling?.userPayment = userPayment
        userPayment?.let { user?.userPaymentList?.add(it) }

        save(user)
    }

    override fun updateUserShipping(userShipping: UserShipping?, user: User?) {
        userShipping?.user = user
        userShipping?.isUserShippingDefault = true
        userShipping?.let { user?.userShippingList?.add(it) }
        save(user)
    }

    override fun setUserDefaultShipping(userShippingId: Long?, client: User?) {
        val userDispatchList = userShippingData?.findAll() as List<UserShipping>

        for (shippingInfo in userDispatchList) {
            if (shippingInfo.id === userShippingId) {
                shippingInfo.isUserShippingDefault = true
                userShippingData.save(shippingInfo)
            } else {
                shippingInfo.isUserShippingDefault = false
                userShippingData.save(shippingInfo)
            }
        }
    }


    override fun setUserDefaultPayment(userPaymentId: Long?, client: User?) {

        val accountPaymentList = customerPaymentRepository?.findAll() as List<UserPayment>

        for (userBilling in accountPaymentList) {
            if (userBilling.id === userPaymentId) {
                userBilling.isDefaultPayment = true
                customerPaymentRepository.save(userBilling)
            } else {
                userBilling.isDefaultPayment = false
                customerPaymentRepository.save(userBilling)
            }
        }
    }




}