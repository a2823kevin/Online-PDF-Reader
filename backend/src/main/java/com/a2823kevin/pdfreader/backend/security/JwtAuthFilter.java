package com.a2823kevin.pdfreader.backend.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.a2823kevin.pdfreader.backend.repository.BlacklistedTokenRepository;
import com.a2823kevin.pdfreader.backend.service.AppUserDetailsService;
import com.a2823kevin.pdfreader.backend.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{
    private final JwtService jwtService;
    private final AppUserDetailsService appUserDetailsService;
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String jwt, username;

        if (authHeader==null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        if (blacklistedTokenRepository.findByToken(jwt).isPresent()) {
            filterChain.doFilter(request, response);
            return;
        }

        username = jwtService.extractUsername(jwt);

        // if (username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
        if (username!=null) {
            UserDetails userDetails = appUserDetailsService.loadUserByUsername(username);
            // check if jwt is valid
            if (jwtService.validateToken(jwt, userDetails)) {
                // if ctx doesn't contain authentication info, set it
                if (SecurityContextHolder.getContext().getAuthentication()==null) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(  
                        userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
            // jwt invalid
            else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired jwt");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}