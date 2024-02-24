package org.demo.chatweb.controllers;

import jakarta.servlet.ServletException;
import org.demo.chatweb.config.JWTUtil;
import org.demo.chatweb.controllers.StatusController;
import org.demo.chatweb.models.User;
import org.demo.chatweb.services.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StatusControllerTests {

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private UsersService usersService;

    @InjectMocks
    private StatusController statusController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSetStatusOnline() throws ServletException {
        String authHeader = "Bearer mockToken";
        User mockUser = new User();
        when(jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader)).thenReturn("mockUsername");
        when(usersService.getByUsername("mockUsername")).thenReturn(mockUser);

        ResponseEntity<Void> responseEntity = statusController.setStatusOnline(authHeader);

        verify(jwtUtil, times(1)).filterAndRetrieveClaimSpecificToken(authHeader);
        verify(usersService, times(1)).getByUsername("mockUsername");
        verify(usersService, times(1)).saveAndSetStatus(mockUser, true);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testSetStatusOffline() throws ServletException {
        String authHeader = "Bearer mockToken";
        User mockUser = new User();
        when(jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader)).thenReturn("mockUsername");
        when(usersService.getByUsername("mockUsername")).thenReturn(mockUser);

        ResponseEntity<Void> responseEntity = statusController.setStatusOffline(authHeader);

        verify(jwtUtil, times(1)).filterAndRetrieveClaimSpecificToken(authHeader);
        verify(usersService, times(1)).getByUsername("mockUsername");
        verify(usersService, times(1)).saveAndSetStatus(mockUser, false);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
