package org.demo.chatweb.controllers;

import jakarta.servlet.ServletException;
import org.demo.chatweb.config.JWTUtil;
import org.demo.chatweb.converters.UserConverter;
import org.demo.chatweb.converters.UserDTOConverter;
import org.demo.chatweb.controllers.ProfileDataController;
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
import static org.mockito.Mockito.when;

class ProfileDataControllerTests {

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private UsersService usersService;

    @Mock
    private UserDTOConverter userDTOConverter;

    @Mock
    private UserConverter userConverter;

    @InjectMocks
    private ProfileDataController profileDataController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testUserProfileData() throws ServletException {
        String authHeader = "Bearer mockToken";

        when(jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader)).thenReturn("mockUsername");

        User mockUser = new User();
        when(usersService.getByUsername("mockUsername")).thenReturn(mockUser);

        UserDTO mockUserDTO = new UserDTO();
        when(userDTOConverter.convertToUserDTO(mockUser)).thenReturn(mockUserDTO);


        ResponseEntity<UserDTO> result = profileDataController.userProfileData(authHeader);


        assertEquals("mockUsername", jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader));
        assertEquals(mockUser, usersService.getByUsername("mockUsername"));
        assertEquals(mockUserDTO, userDTOConverter.convertToUserDTO(mockUser));


        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(mockUserDTO, result.getBody());
    }
}
