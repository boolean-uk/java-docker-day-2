package com.booleanuk.api.security.services;

import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Service component in spring context
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    /*
    Implements the UserDetailsService interface,
    which is used by Spring Security during authentication to load user-specific data.
     */

    UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*
        Returns a UserDetails object by building it from the found User entity using UserDetailsImpl.build(user).
         */
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User not found with username: " + username)
        );
        return UserDetailsImpl.build(user);
    }
}