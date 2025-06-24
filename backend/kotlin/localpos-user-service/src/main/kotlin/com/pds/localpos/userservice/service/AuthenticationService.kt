package com.pds.localpos.userservice.service

interface AuthenticationService {
    fun authenticate(username: String, password: String): String
}
