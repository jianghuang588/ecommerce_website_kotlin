package com.Admin.domain

import com.Admin.domain.security.Authority
import com.Admin.domain.security.UserRole
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.function.Consumer
import javax.persistence.*


// table that represent entity
// UserDetails allow user to further the security
// such as verify username or passowrd
@Entity
class User : UserDetails {

    // represent the unique primary key
    @Id
    // generate sequence number for primary key
    @GeneratedValue(strategy = GenerationType.AUTO)
    // after the sequal number is insert for primary key, it become
    // unchangeable
    @Column(name = "id", nullable = false, updatable = false)
    var id: Long? = null
        get() = field
        set(value) {
            field = value
        }


    @OneToOne(cascade = [CascadeType.ALL], mappedBy = "user")
    var shoppingCart: ShoppingCart? = null

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user")
    var userShippingList: MutableList<UserShipping>? =mutableListOf()


    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "user")
    var userPaymentList: MutableList<UserPayment>? = mutableListOf()



    // user can have many userrole permission
    // cascade = [CascadeType.ALL] mean if user parent is delete than parent child
    // will be delete
    // fetch = FetchType.EAGER mean if parent load, that child also load
    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    // protect sensitive data from showing
    @JsonIgnore
    internal var userRoles: Set<UserRole> = HashSet()
        get() = field
        set(value) {
            field = value
        }

    // get read the data from mysql
    // set update the data from mysql
    @Column(nullable = false, unique = true)
    private var username: String = ""

    fun setUsername(value: String) {
        username = value
    }

    override fun getUsername(): String {
        return username
    }


    @Column(nullable = false)
    private var password: String = ""

    fun setPassword(value: String) {
        password = value
    }

    override fun getPassword(): String {
        return password
    }


    var firstName: String = ""
        get() = field
        set(value) {
            field = value
        }

    var lastName: String = ""
        get() = field
        set(value) {
            field = value
        }

    // once email is register, unchangeable
    @Column(name = "email", nullable = false, updatable = false)
    var email: String = ""
        get() = field
        set(value) {
            field = value
        }

    var phone: String = ""
        get() = field
        set(value) {
            field = value
        }


    // user is alway avaialble
    var enable: Boolean = true
        get() = field
        set(value) {
            field = value
        }


    // it convert user role to into GrantedAuthority so user spring boot securit
    // can set the security logic
    override fun getAuthorities(): MutableCollection<out GrantedAuthority?>? {
        val access: MutableSet<GrantedAuthority> = HashSet()
        userRoles.forEach(Consumer { your: UserRole ->
            access.add(
                Authority(
                    your.role!!.name!!
                )
            )
        })
        return access
    }

    // check account expire
    override fun isAccountNonExpired(): Boolean {
        return true
    }

    // check account is lock
    override fun isAccountNonLocked(): Boolean {
        return true
    }

    // check password is still valid
    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    // check the account is valid
    override fun isEnabled(): Boolean {
        return this.enable
    }





}