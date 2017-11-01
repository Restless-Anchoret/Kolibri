package com.ran.kolibri.security

import com.ran.kolibri.entity.user.UserRole
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserData(
        val id: Long,
        val login: String,
        val userRole: UserRole
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf()
    override fun isEnabled(): Boolean = true
    override fun getUsername(): String = login
    override fun isCredentialsNonExpired(): Boolean = true
    override fun getPassword(): String = ""
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true

}
