package org.demo.chatweb.controllers;

import org.demo.chatweb.converters.UserConverter;
import org.demo.chatweb.controllers.RegistrationController;
import org.demo.chatweb.dto.UserDTO;
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

class RegistrationControllerTests {

    @Mock
    private UsersService usersService;

    @Mock
    private UserConverter userConverter;

    @InjectMocks
    private RegistrationController registrationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testSaveUser() {
        UserDTO userDTO = new UserDTO("JohnDoe", "john.doe@example.com", "password");

        User convertedUser = new User();
        when(userConverter.convertToUser(userDTO)).thenReturn(convertedUser);

        doNothing().when(usersService).save(any());

        ResponseEntity result = registrationController.save(userDTO);

        verify(userConverter, times(1)).convertToUser(userDTO);
        verify(usersService, times(1)).save(convertedUser);

        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
