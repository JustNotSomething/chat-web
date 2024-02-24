package org.demo.chatweb.controllers;

import jakarta.servlet.ServletException;
import org.demo.chatweb.config.JWTUtil;
import org.demo.chatweb.converters.ChatConverter;
import org.demo.chatweb.converters.ListMessageDTOConverter;
import org.demo.chatweb.dto.MessageDTO;
import org.demo.chatweb.dto.UserDTO;
import org.demo.chatweb.models.Chat;
import org.demo.chatweb.models.Message;
import org.demo.chatweb.models.User;
import org.demo.chatweb.services.ChatsService;
import org.demo.chatweb.services.MessagesService;
import org.demo.chatweb.services.StorageService;
import org.demo.chatweb.services.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ChatsControllerTests {

    @Mock
    private ChatsService chatsService;

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private UsersService usersService;

    @Mock
    private ChatConverter chatConverter;

    @Mock
    private MessagesService messagesService;

    @Mock
    private ListMessageDTOConverter listMessageDTOConverter;

    @Mock
    private StorageService storageService;

    @Spy
    @InjectMocks
    private ChatsController chatsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFriends() throws ServletException {

        String authHeader = "Bearer mockToken";
        String username = "mockUser";
        User mockUser = new User();
        List<Chat> mockChats = Arrays.asList(new Chat());
        List<UserDTO> mockUserChats = Arrays.asList(new UserDTO());


        when(jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader)).thenReturn(username);
        when(usersService.getByUsername(username)).thenReturn(mockUser);
        when(chatsService.findUserChats(mockUser)).thenReturn(mockChats);
        when(chatConverter.convertToChatList(mockUser, mockChats)).thenReturn(mockUserChats);


        ResponseEntity<List<UserDTO>> responseEntity = chatsController.getFriends(authHeader);

        verify(chatsController).getFriends(authHeader);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockUserChats, responseEntity.getBody());
    }


    @Test
    void testGetFriendsException() throws ServletException {
        String authHeader = "Bearer mockToken";

        when(jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader)).thenThrow(new ServletException("Mock exception"));

        assertThrows(ServletException.class, () -> chatsController.getFriends(authHeader));
    }


    @Test
    void testGetLastMessages() throws Exception {
        // Mock data
        String authHeader = "Bearer mockToken";
        String username = "mockUser";
        User mockUser = new User();
        List<MessageDTO> mockMessageDTOs = Arrays.asList(new MessageDTO());

        // Mock behavior
        when(jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader)).thenReturn(username);
        when(usersService.getByUsername(username)).thenReturn(mockUser);
        when(messagesService.findLastMessagesForChats()).thenReturn(Arrays.asList(new Message()));
        when(listMessageDTOConverter.convertList(anyList())).thenReturn(mockMessageDTOs);

        // Perform the test
        List<MessageDTO> result = chatsController.getLastMessages(authHeader);

        // Verify the result
        assertEquals(mockMessageDTOs, result);
    }

}
