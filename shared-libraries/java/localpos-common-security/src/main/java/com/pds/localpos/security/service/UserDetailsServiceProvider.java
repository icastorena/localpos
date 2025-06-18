package com.pds.localpos.security.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailsServiceProvider {

    UserDetails loadUserByUsername(String username);
}
