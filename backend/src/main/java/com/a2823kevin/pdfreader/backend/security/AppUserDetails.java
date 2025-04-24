package com.a2823kevin.pdfreader.backend.security;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.a2823kevin.pdfreader.backend.model.User;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AppUserDetails implements UserDetails{
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
        .map(role->new SimpleGrantedAuthority("ROLE_"+role.getName()))
        .collect(Collectors.toList());
    }

    public Long getId() {
        return user.getId();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    
}