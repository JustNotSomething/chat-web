package org.demo.chatweb.services;

import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import org.demo.chatweb.models.Chat;
import org.demo.chatweb.models.Section;
import org.demo.chatweb.models.User;
import org.demo.chatweb.repository.ChatsRepository;
import org.demo.chatweb.repository.MessagesRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@SpringJUnitConfig
@DataJpaTest
public class ChatsServiceTests {

    @Mock
    private ChatsRepository chatsRepository;

    @Mock
    private MessagesRepository messagesRepository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ChatsService chatsService;

    @Test
    public void ChatsService_SaveChat_CallsRepositorySave() {
        Chat chat = new Chat();

        chatsService.save(chat);

        Mockito.verify(chatsRepository, times(1)).save(chat);
    }

    @Test
    public void ChatsService_GetAllChats_ReturnAllChats() {
        List<Chat> chatList = new ArrayList<>();
        Mockito.when(chatsRepository.findAll()).thenReturn(chatList);

        List<Chat> result = chatsService.index();

        assertEquals(chatList, result);
    }

    @Test
    public void ChatsService_RemoveChat_CallsRepositoryDelete() {
        User user1 = new User();
        User user2 = new User();
        List<Chat> existingChats = Collections.singletonList(new Chat());

        Mockito.when(chatsRepository.findExistingChats(user1, user2)).thenReturn(existingChats);

        chatsService.removeChat(user1, user2);

        Mockito.verify(chatsRepository, times(1)).delete(existingChats.get(0));
    }

    @Test
    public void ChatsService_RemoveChat_ThrowsExceptionIfNoExistingChats() {
        User user1 = new User();
        User user2 = new User();

        Mockito.when(chatsRepository.findExistingChats(user1, user2)).thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class, () -> chatsService.removeChat(user1, user2));
    }



    @Test
    public void ChatsService_CreateChat_SavesTwoChats() throws ServletException, ServletException {
        User user1 = new User();
        User user2 = new User();

        Mockito.when(chatsService.findExistingChat(user1, user2)).thenReturn(null);

        chatsService.createChat(user1, user2);

        Mockito.verify(chatsRepository, times(2)).save(Mockito.any(Chat.class));
    }


}
