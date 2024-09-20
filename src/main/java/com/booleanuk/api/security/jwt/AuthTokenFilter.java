package com.booleanuk.api.security.jwt;

import com.booleanuk.api.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
    // Extends OncePerRequestFilter, ensuring that the filter is executed once per request.

    // To load user details.
    private final UserDetailsServiceImpl userDetailsService;

    // For handling JWT operations.
    private final JwtUtils jwtUtils;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);



    public AuthTokenFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        /*
         Intercepts incoming HTTP requests to validate JWT tokens.
         */
        try {
            // Calls parseJwt(request) to extract the JWT token from the Authorization header.
            String jwt = parseJwt(request);

            // Checks if the token is present and valid using jwtUtils.validateJwtToken(jwt).
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {

                // Retrieves the username from the token using jwtUtils.getUserNameFromJwtToken(jwt).
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // Loads the user details using userDetailsService.loadUserByUsername(username).
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Creates an UsernamePasswordAuthenticationToken and sets it in the SecurityContext.
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}