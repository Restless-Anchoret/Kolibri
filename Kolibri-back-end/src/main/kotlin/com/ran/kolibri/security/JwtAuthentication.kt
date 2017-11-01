package com.ran.kolibri.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class JwtAuthentication(
        private val userData: UserData
) : Authentication {

    override fun getAuthorities(): Collection<GrantedAuthority> = listOf()
    override fun getCredentials(): Any = ""
    override fun getDetails(): Any = ""
    override fun getPrincipal(): Any = userData
    override fun isAuthenticated(): Boolean = true
    override fun setAuthenticated(authenticated: Boolean) { }
    override fun getName(): String = ""

}
