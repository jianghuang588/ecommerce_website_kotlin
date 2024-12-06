package com.Admin.service.impl

import com.Admin.domain.User
import com.Admin.domain.security.PasswordResetToken
import com.Admin.domain.security.UserRole
import com.Admin.repository.PasswordResetTokenRepository
import com.Admin.repository.RoleRepository
import com.Admin.repository.UserRepository
import com.Admin.service.UserService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.function.Consumer


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
    override fun getPasswordResetToken(key: String?): PasswordResetToken? {
        return resetTokenRepository!!.findByToken(key)
    }

    @Autowired // userRepository
    private val clientRepository: UserRepository? = null

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

    override fun createUser(client: User?, accountRoles: Set<UserRole?>?): User? {
        var internalUser = userDirectory!!.findByUsername(client?.getUsername())

        if (internalUser != null) {
            LOG.info("Client {} already exists. No action is needed.", client?.getUsername())
        } else {
            for (ur in accountRoles!!) {
                userRoleRepository!!.save(ur?.role!!)            }

            //client.getUserRoles().addAll(accountRoles)

            client!!.userRoles = client.userRoles + (accountRoles?.filterNotNull() ?: emptySet())


            /*
            val cart: ShoppingCart = ShoppingCart()
            cart.setUser(client)
            client.setShoppingCart(cart)

            client.setUserShippingList(ArrayList<UserShipping>())
            client.setUserPaymentList(ArrayList<UserPayment>())

            */
            internalUser = userDirectory.save(client!!)
        }
        return internalUser
    }

    override fun save(client: User?): User {
        return userDirectory!!.save(client!!)
    }


    override fun getAllUsers(): List<User?> {
        return clientRepository!!.findAll()
    }

    // id
    override fun lockUserById(identity: Long?) {
        // user
        val client = clientRepository!!.findById(identity!!).orElseThrow {
            RuntimeException(
                "Client not found"
            )
        }!!!!
        client.enable = false
        clientRepository!!.save(client)
    }

    // id
    override fun unlockUserById(identity: Long?) {
        // user
        val client = clientRepository!!.findById(identity!!).orElseThrow {
            java.lang.RuntimeException(
                "Client not found"
            )
        }!!!!
        client.enable = true
        clientRepository!!.save(client)
    }

    // userIds
    override fun lockUsersByIds(userIds: List<Long?>?) {
        userIds?.forEach(Consumer<Long?> { identity: Long? ->
            // user
            val client = clientRepository!!.findById(identity!!).orElseThrow {
                java.lang.RuntimeException(
                    "Client not found"
                )
            }!!!!
            client.enable = false
            clientRepository!!.save(client)
        })
    }

    // userIds
    override fun unlockUsersByIds(userIds: List<Long?>?) {
        userIds?.forEach(Consumer<Long?> { identity: Long? ->
            // user
            val client = clientRepository!!.findById(identity!!).orElseThrow {
                java.lang.RuntimeException(
                    "Client not found"
                )
            }!!!!
            client.enable = true
            clientRepository!!.save(client)
        })
    }


}