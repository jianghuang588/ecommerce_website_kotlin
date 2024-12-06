package com.Admin.domain.security

import javax.persistence.*


// role table
@Entity
class Role {

    // set unique id for the table
    @Id
    var roleId = 0
        get() = field
        set(value) {
            field = value
        }

    // create column for name on the table
    var name: String? = null
        get() = field
        set(value) {
            field = value
        }

    //
    @OneToMany(mappedBy = "role", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var userRoles: Set<UserRole> = HashSet()
        get() = field
        set(value) {
            field = value
        }



}