package com.ideas2it.maintenanceservice.controller;

import com.ideas2it.maintenanceservice.controller.AuthController.LoginRequest;
import com.ideas2it.maintenanceservice.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @InjectMocks AuthController authController;
    @Mock AuthenticationManager authenticationManager;
    @Mock JwtTokenProvider jwtTokenProvider;
    private AuthController.LoginRequest loginRequest;
    private org.springframework.security.core.userdetails.User userDetails;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setUsername("user1");
        loginRequest.setPassword("password123");
        userDetails = new User("user1", "password123", Set.of((GrantedAuthority) () -> "EMPLOYEE"));
        authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Test
    void login_success() throws Exception {
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtTokenProvider.generateToken(anyString(), anySet())).thenReturn("jwt-token");
        org.springframework.http.ResponseEntity<AuthController.LoginResponse> response = authController.login(loginRequest);
        org.junit.jupiter.api.Assertions.assertEquals(org.springframework.http.HttpStatus.OK, response.getStatusCode());
        org.junit.jupiter.api.Assertions.assertEquals("jwt-token", response.getBody().getToken());
    }

    @Test
    void login_invalidCredentials_throwsIllegalArgumentException() {
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));
        assertThrows(IllegalArgumentException.class, () -> authController.login(loginRequest));
    }
} 