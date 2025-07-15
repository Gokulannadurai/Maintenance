package com.ideas2it.maintenanceservice.controller;

import com.ideas2it.maintenanceservice.security.JwtTokenProvider;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Authentication controller for login and JWT issuance.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * Login endpoint for authenticating users and issuing JWT.
     * @param loginRequest the login request (username, password)
     * @return JWT token and roles if authentication is successful
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("API: Login attempt for user: {}", loginRequest.getUsername());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            Set<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());
            String token = jwtTokenProvider.generateToken(userDetails.getUsername(), roles);
            return ResponseEntity.ok(new LoginResponse(token, roles));
        } catch (BadCredentialsException ex) {
            log.warn("API: Invalid credentials for user: {}", loginRequest.getUsername());
            throw new IllegalArgumentException("Invalid username or password");
        }
    }

    /**
     * Login request DTO.
     */
    @Data
    public static class LoginRequest {
        @NotBlank
        private String username;
        @NotBlank
        private String password;
    }

    /**
     * Login response DTO.
     */
    @Data
    public static class LoginResponse {
        private final String token;
        private final Set<String> roles;
    }
} 