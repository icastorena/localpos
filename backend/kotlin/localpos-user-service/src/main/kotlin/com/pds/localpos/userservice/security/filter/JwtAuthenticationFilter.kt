package com.pds.localpos.userservice.security.filter

import com.pds.localpos.userservice.security.util.JwtTokenProvider
import com.pds.localpos.userservice.service.UserDetailsServiceProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val tokenProvider: JwtTokenProvider,
    private val userDetailsServiceProvider: UserDetailsServiceProvider
) : OncePerRequestFilter() {

    companion object {
        private const val HEADER_STRING = "Authorization"
        private const val TOKEN_PREFIX = "Bearer "
        private val log = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header = request.getHeader(HEADER_STRING)

        if (header != null && header.startsWith(TOKEN_PREFIX)) {
            val token = header.removePrefix(TOKEN_PREFIX)

            if (tokenProvider.validateToken(token)) {
                val username = tokenProvider.getUsernameFromToken(token)
                val userDetails = userDetailsServiceProvider.loadUserByUsername(username)

                val authentication = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.authorities
                )

                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            }
        }

        filterChain.doFilter(request, response)
    }
}
