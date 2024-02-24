package org.demo.chatweb.repository;

import org.assertj.core.api.Assertions;
import org.demo.chatweb.models.Chat;
import org.demo.chatweb.models.Message;
import org.demo.chatweb.models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class MessagesRepositoryTests {

    @Mock
    private MessagesRepository messagesRepository;

    @Mock
    private User user;

    @Mock
    private Chat chat;

    @Test
    public void MessagesRepositoryTests_FindAllByChat_ChatId_ReturnListOfMessages() {
        Message message1 = new Message(chat, user, new User(), "Hello", new Date());
        Message message2 = new Message(chat, user, new User(), "Hi", new Date());
        List<Message> messageList = Arrays.asList(message1, message2);

        Mockito.when(messagesRepository.findAllByChat_ChatId(1)).thenReturn(messageList);

        List<Message> result = messagesRepository.findAllByChat_ChatId(1);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void MessagesRepositoryTests_DeleteUserAllMessages_ShouldNotThrowException() {
        Mockito.doNothing().when(messagesRepository).deleteUserAllMessages(user);

        Assertions.assertThatCode(() -> messagesRepository.deleteUserAllMessages(user)).doesNotThrowAnyException();
    }
}
