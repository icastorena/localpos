package com.pds.localpos.userservice.service

import org.springframework.security.core.userdetails.UserDetails

interface UserDetailsServiceProvider {

    fun loadUserByUsername(username: String): UserDetails
}
