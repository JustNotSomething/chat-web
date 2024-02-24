package org.demo.chatweb.controllers;

import jakarta.servlet.ServletException;
import org.demo.chatweb.config.JWTUtil;
import org.demo.chatweb.converters.ChatConverter;
import org.demo.chatweb.converters.UserConverter;
import org.demo.chatweb.converters.UserDTOConverter;
import org.demo.chatweb.dto.UserDTO;
import org.demo.chatweb.dto.VisibilityDTO;
import org.demo.chatweb.models.Chat;
import org.demo.chatweb.models.User;
import org.demo.chatweb.services.ChatsService;
import org.demo.chatweb.services.MessagesService;
import org.demo.chatweb.services.StorageService;
import org.demo.chatweb.services.UsersService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsersControllerTests {

    @Mock
    private ChatsService chatsService;

    @Mock
    private UsersService usersService;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private ChatConverter chatConverter;

    @Mock
    private UserDTOConverter userDTOConverter;

    @Mock
    private UserConverter userConverter;

    @Mock
    private StorageService storageService;

    @Mock
    private MessagesService messagesService;

    @InjectMocks
    private UsersController usersController;

    @Test
    void testGetAllUnknownUsers() throws AccessDeniedException, ServletException {

        when(jwtUtil.filterAndRetrieveClaimSpecificToken(anyString())).thenReturn("admin");
        when(usersService.getByUsername("admin")).thenReturn(new User("admin", "ROLE_ADMIN"));

        ResponseEntity<List<UserDTO>> responseEntity = usersController.getAllUnknownUsers("token");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    @Test
    void testGetUnknownUsers() throws ServletException, IOException {

        when(jwtUtil.filterAndRetrieveClaimSpecificToken(anyString())).thenReturn("user");
        User user = new User("user", "ROLE_USER");
        when(usersService.getByUsername("user")).thenReturn(user);

        List<Chat> userChats = new ArrayList<>();
        when(chatsService.findUserChats(user)).thenReturn(userChats);


        ResponseEntity<List<UserDTO>> responseEntity = usersController.getUnknownUsers("token");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }


    @Test
    void testAddFriendRelation() throws ServletException {
        // Mocking
        when(jwtUtil.filterAndRetrieveClaimSpecificToken(anyString())).thenReturn("user");
        User user1 = new User("user", "ROLE_USER");
        when(usersService.getByUsername("user")).thenReturn(user1);

        User user2 = new User("friend", "ROLE_USER");
        when(usersService.find(Mockito.anyInt())).thenReturn(user2);

        ResponseEntity responseEntity = usersController.addFriendRelation("token", 123);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testRemoveRelation() throws ServletException {
        when(jwtUtil.filterAndRetrieveClaimSpecificToken(anyString())).thenReturn("user");
        User user1 = new User("user", "ROLE_USER");
        when(usersService.getByUsername("user")).thenReturn(user1);

        User user2 = new User("friend", "ROLE_USER");
        when(usersService.find(Mockito.anyInt())).thenReturn(user2);

        ResponseEntity responseEntity = usersController.removeRelation("token", 123);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


    @Test
    void testDeleteUser() throws ServletException {
        when(jwtUtil.filterAndRetrieveClaimSpecificToken(anyString())).thenReturn("user");
        when(usersService.getByUsername("user")).thenReturn(new User("user", "ROLE_USER"));

        ResponseEntity<HttpStatus> responseEntity = usersController.deleteUser("token");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testUpdateUserProfile() throws ServletException {
        when(jwtUtil.filterAndRetrieveClaimSpecificToken(anyString())).thenReturn("user");
        when(usersService.getByUsername("user")).thenReturn(new User("user", "ROLE_USER"));
        when(userConverter.convertToUser(Mockito.any(UserDTO.class))).thenReturn(new User());

        when(jwtUtil.generateToken(
                ArgumentMatchers.nullable(String.class),
                ArgumentMatchers.eq("ROLE_USER"),
                ArgumentMatchers.nullable(String.class)
        )).thenReturn("some-token");

        Map<String, String> result = usersController.updateUserProfile("token", new UserDTO());

        assertEquals("some-token", result.get("jwt-token"));
    }

    @Test
    void testHideProfile() throws ServletException {
        when(jwtUtil.filterAndRetrieveClaimSpecificToken(anyString())).thenReturn("user");
        when(usersService.getByUsername("user")).thenReturn(new User("user", "ROLE_USER"));

        ResponseEntity<HttpStatus> responseEntity = usersController.hideProfile("token");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testRevealProfile() throws ServletException {
        when(jwtUtil.filterAndRetrieveClaimSpecificToken(anyString())).thenReturn("user");
        when(usersService.getByUsername("user")).thenReturn(new User("user", "ROLE_USER"));

        ResponseEntity<HttpStatus> responseEntity = usersController.revealProfile("token");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testGetProfileVisibility() throws ServletException {
        when(jwtUtil.filterAndRetrieveClaimSpecificToken(anyString())).thenReturn("user");

        User user = new User("user", "ROLE_USER");
        user.setHideProfile(false);

        when(usersService.getByUsername("user")).thenReturn(user);

        ResponseEntity<VisibilityDTO> responseEntity = usersController.isVisible("token");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testDeleteUserAccount() throws ServletException {
        when(jwtUtil.filterAndRetrieveClaimSpecificToken(anyString())).thenReturn("admin");
        when(usersService.getByUsername("admin")).thenReturn(new User("admin", "ROLE_ADMIN"));
        when(usersService.find(Mockito.anyInt())).thenReturn(new User());

        ResponseEntity<HttpStatus> responseEntity = usersController.deleteUserAccount("token", 123);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testChangeUserRole() throws ServletException {
        when(jwtUtil.filterAndRetrieveClaimSpecificToken(anyString())).thenReturn("admin");
        when(usersService.getByUsername("admin")).thenReturn(new User("admin", "ROLE_ADMIN"));
        when(usersService.find(Mockito.anyInt())).thenReturn(new User());

        ResponseEntity<HttpStatus> responseEntity = usersController.changeUserRole("token", 123);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

}
