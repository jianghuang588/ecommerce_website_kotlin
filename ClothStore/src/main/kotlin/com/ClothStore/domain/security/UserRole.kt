package com.ClothStore.domain.security

import com.ClothStore.domain.User
import javax.persistence.*


// current entity has the table name
// call user_role
@Entity
@Table(name="user_role")
class UserRole {
    // generate sequence unique id
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null
        // get retrive the mysql data
        // set update the mysql data
        get() = field
        set(value) {
            field = value
        }

    // user_role table can have many user
    // join by user_id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    var user: User? = null
        get() = field
        set(value) {
            field = value
        }
    // user_role table can have many role
    // join by role_id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id")
    var role: Role? = null
        get() = field
        set(value) {
            field = value
        }
    /*
    fun UserRole() {}

    fun UserRole(user: User?, role: Role) {
        this.user = user
        this.role = role
    }
    */
    constructor()
    constructor(user: User?, role: Role) {
        this.user = user
        this.role = role
    }







}