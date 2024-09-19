# Spring Security Configuration Overview

This document provides an overview of the security configuration for a Spring Boot application using JWT (JSON Web Token) authentication and role-based access control.

## Table of Contents
1. [Security Configuration (WebSecurityConfig)](#security-configuration-websecurityconfig)
2. [User Details](#user-details)
3. [JWT Handling](#jwt-handling)
4. [Authentication Filter](#authentication-filter)
5. [Error Handling](#error-handling)

## Security Configuration (WebSecurityConfig)

The `WebSecurityConfig` class is the core of the security setup, configuring various aspects of the application's security.

### Authentication Provider
- Uses `DaoAuthenticationProvider` to authenticate users with a username and password.
- Configured with a custom `UserDetailsServiceImpl` and `BCryptPasswordEncoder`.

### Filters
- `AuthTokenFilter` is added to the security filter chain to intercept and validate JWT tokens.

### Role-Based Access Control
- Uses roles (USER, ADMIN) to restrict access to certain endpoints based on the user's role.

### Stateless Sessions
- Configured to be stateless because JWT tokens are used for authentication instead of server-side sessions.

## User Details

### Custom UserDetails Implementation
- `UserDetailsImpl` represents the authenticated user's details.

### User Details Service
- `UserDetailsServiceImpl` is used by Spring Security to load user details during authentication.

## JWT Handling

The `JwtUtils` class manages all JWT-related operations.

### Token Generation
- Generates JWT tokens upon successful authentication, embedding the username as the subject.

### Token Validation
- Validates tokens on each request to ensure they are valid and not expired.

### Token Parsing
- Extracts user information from the token for authorization purposes.

## Authentication Filter

The `AuthTokenFilter` class handles the interception and processing of JWT tokens in requests.

### Request Interception
- Intercepts incoming HTTP requests to extract and validate JWT tokens.

### Security Context Setup
- Sets up the `SecurityContext` with the authenticated user's details if the token is valid.

## Error Handling

The `AuthEntryPointJwt` class manages unauthorized access attempts.

### Unauthorized Access
- Handles cases where a user tries to access a protected resource without proper authentication.
- Sends a 401 Unauthorized response.