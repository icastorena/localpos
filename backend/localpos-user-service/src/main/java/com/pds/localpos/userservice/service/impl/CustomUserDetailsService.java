package com.pds.localpos.userservice.service.impl;

import com.pds.localpos.security.service.UserDetailsServiceProvider;
import com.pds.localpos.userservice.model.LocalPosUserDetails;
import com.pds.localpos.userservice.model.User;
import com.pds.localpos.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsServiceProvider, UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new LocalPosUserDetails(user);
    }
}
