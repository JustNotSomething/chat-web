package org.demo.chatweb.controllers;

import org.demo.chatweb.config.JWTUtil;
import org.demo.chatweb.dto.AuthenticationDTO;
import org.demo.chatweb.models.User;
import org.demo.chatweb.services.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoginControllerTests {

    @Mock
    private UsersService usersService;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testLoginSuccess() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO("username", "password");
        when(authenticationManager.authenticate(any())).thenReturn(null);

        User user = new User();
        user.setUsername("username");
        user.setRole("USER");
        user.setEmail("user@example.com");
        when(usersService.getByUsername("username")).thenReturn(user);

        when(jwtUtil.generateToken("username", "USER", "user@example.com")).thenReturn("mockedToken");

        Map<String, String> result = loginController.login(authenticationDTO);

        assertEquals("mockedToken", result.get("jwt-token"));
    }

    @Test
    void testLoginFailure() {
        AuthenticationDTO authenticationDTO = new AuthenticationDTO("invalidUsername", "invalidPassword");
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));

        Map<String, String> result = loginController.login(authenticationDTO);

        assertEquals("error", result.get("message"));
    }
}
