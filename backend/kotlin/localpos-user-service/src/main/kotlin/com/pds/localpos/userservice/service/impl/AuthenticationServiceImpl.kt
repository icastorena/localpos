package com.pds.localpos.userservice.service.impl

import com.pds.localpos.userservice.security.util.JwtTokenProvider
import com.pds.localpos.userservice.service.AuthenticationService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class AuthenticationServiceImpl(
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider
) : AuthenticationService {

    override fun authenticate(username: String, password: String): String {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(username, password)
        )
        val userDetails = authentication.principal as UserDetails
        return jwtTokenProvider.generateToken(userDetails)
    }
}
