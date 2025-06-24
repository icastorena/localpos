package com.pds.localpos.userservice.service.impl

import com.pds.localpos.userservice.model.LocalPosUserDetails
import com.pds.localpos.userservice.repository.UserRepository
import com.pds.localpos.userservice.service.UserDetailsServiceProvider
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsServiceProvider, UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            .orElseThrow { UsernameNotFoundException("User not found: $username") }
        return LocalPosUserDetails(user)
    }
}
