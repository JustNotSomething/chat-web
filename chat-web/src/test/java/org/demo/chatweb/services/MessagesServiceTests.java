package org.demo.chatweb.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.demo.chatweb.models.Chat;
import org.demo.chatweb.models.Message;
import org.demo.chatweb.models.User;
import org.demo.chatweb.repository.MessagesRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@SpringJUnitConfig
@DataJpaTest
public class MessagesServiceTests {

    @Mock
    private MessagesRepository messagesRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private MessagesService messagesService;

    @Test
    public void MessagesService_SaveMessage_CallsRepositorySave() {
        Message message = new Message();

        messagesService.save(message);

        Mockito.verify(messagesRepository, Mockito.times(1)).save(message);
    }

    @Test
    public void MessagesService_GetChatMessages_ReturnsChatMessages() {
        Chat chat = new Chat();
        chat.setChatId(1);

        List<Message> messageList = new ArrayList<>();
        Mockito.when(messagesRepository.findAllByChat_ChatId(chat.getChatId())).thenReturn(messageList);

        List<Message> result = messagesService.getChatMessages(chat);

        assertEquals(messageList, result);
    }

    @Test
    public void MessagesService_DeleteAllUsersMessages_CallsRepositoryDelete() {
        User user = new User();

        messagesService.deleteAllUsersMessages(user);

        Mockito.verify(messagesRepository, Mockito.times(1)).deleteUserAllMessages(user);
    }

}
