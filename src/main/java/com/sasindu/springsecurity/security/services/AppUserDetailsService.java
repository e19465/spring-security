package com.sasindu.springsecurity.security.services;

import com.sasindu.springsecurity.exceptions.NotFoundException;
import com.sasindu.springsecurity.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service to load user details
 */
@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    private final IUserRepository _userRepository;


    /**
     * Load user by username
     *
     * @param email The email of the user
     * @return The user details
     * @throws UsernameNotFoundException If the user is not found
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            return  Optional.ofNullable(_userRepository.findByEmail(email))
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        } catch (UsernameNotFoundException e) {
            throw new NotFoundException("User not found");
        }
    }
}
