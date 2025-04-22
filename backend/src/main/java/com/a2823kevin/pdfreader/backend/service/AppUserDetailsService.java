package com.a2823kevin.pdfreader.backend.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.a2823kevin.pdfreader.backend.model.User;
import com.a2823kevin.pdfreader.backend.repository.UserRepository;
import com.a2823kevin.pdfreader.backend.security.AppUserDetails;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AppUserDetailsService implements UserDetailsService{
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        user.orElseThrow(() -> new UsernameNotFoundException("User not found with name" + username));
        return new AppUserDetails(user.get());
    }
}
