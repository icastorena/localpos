package com.pds.localpos.userservice.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class LocalPosUserDetails(user: User) : UserDetails {

    val id: Long? = user.id
    private val username: String = user.username
    private val password: String = user.password
    private val authorities: List<GrantedAuthority> = user.roles.map { role ->
        SimpleGrantedAuthority("ROLE_${role.name}")
    }

    override fun getAuthorities(): Collection<GrantedAuthority> = authorities

    override fun getPassword(): String = password

    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
