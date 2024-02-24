package org.demo.chatweb.controllers;

import org.demo.chatweb.controllers.ChatController;
import org.demo.chatweb.dto.MessageDTO;
import org.demo.chatweb.models.Chat;
import org.demo.chatweb.models.Message;
import org.demo.chatweb.models.User;
import org.demo.chatweb.services.ChatsService;
import org.demo.chatweb.services.MessagesService;
import org.demo.chatweb.services.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChatControllerTests {

    @Mock
    private UsersService usersService;

    @Mock
    private ChatsService chatsService;

    @Mock
    private MessagesService messagesService;

    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @InjectMocks
    private ChatController chatController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        chatController = new ChatController(usersService, chatsService, messagesService, simpMessagingTemplate);
    }


    @Test
    public void testSendMessage() {

        String senderUsername = "sender";
        String receiverUsername = "receiver";
        MessageDTO payload = new MessageDTO();
        payload.setSenderUsername(senderUsername);
        payload.setReceiverUsername(receiverUsername);
        payload.setMessage("Hello!");

        Chat chat1 = new Chat();
        Chat chat2 = new Chat();
        List<Chat> existingChats = Arrays.asList(chat1, chat2);


        User user1 = new User();
        user1.setOnline(false);
        user1.setRole("ROLE_USER");
        user1.setUsername(senderUsername);
        user1.setPhone("+11111111111");
        user1.setPassword("1111");
        user1.setEmail("some@gmail.com");
        user1.setHideProfile(true);
        user1.setDateOfBirth(new Date());

        User user2 = new User();
        user2.setOnline(false);
        user2.setRole("ROLE_USER");
        user2.setUsername(receiverUsername);
        user2.setPhone("+11111111111");
        user2.setPassword("1111");
        user2.setEmail("some@gmail.com");
        user2.setHideProfile(true);
        user2.setDateOfBirth(new Date());

        when(usersService.getByUsername(senderUsername)).thenReturn(user1);
        when(usersService.getByUsername(receiverUsername)).thenReturn(user2);
        when(chatsService.findExistingChat(any(), any())).thenReturn(existingChats);


        chatController.sendMessage(payload, receiverUsername);


        verify(messagesService, Mockito.times(2)).save(any(Message.class));

        verify(simpMessagingTemplate).convertAndSend("/user/" + receiverUsername + "/message", payload);
    }
}
