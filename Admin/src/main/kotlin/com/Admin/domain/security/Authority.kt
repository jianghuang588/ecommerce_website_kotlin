package com.Admin.domain.security

import org.springframework.security.core.GrantedAuthority

// GrantedAuthority provide by springboot that
// check user has permission to login
class Authority(private val control: String) : GrantedAuthority{
    override fun getAuthority(): String {
        return control
    }
}