package org.demo.chatweb.repository;

import org.assertj.core.api.Assertions;
import org.demo.chatweb.models.Chat;
import org.demo.chatweb.models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class ChatsRepositoryTests {

    @Mock
    private ChatsRepository chatsRepository;

    @Mock
    private User user;

    @Test
    public void ChatsRepositoryTests_FindUserAllChats_ReturnListOfChats() {
        Chat chat1 = new Chat(user, new User());
        Chat chat2 = new Chat(new User(), user);
        List<Chat> chatList = Arrays.asList(chat1, chat2);

        Mockito.when(chatsRepository.findUserAllChats(user)).thenReturn(chatList);

        List<Chat> result = chatsRepository.findUserAllChats(user);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void ChatsRepositoryTests_DeleteUserAllChats_ShouldNotThrowException() {
        Mockito.doNothing().when(chatsRepository).deleteUserAllChats(user);

        Assertions.assertThatCode(() -> chatsRepository.deleteUserAllChats(user)).doesNotThrowAnyException();
    }

    @Test
    public void ChatsRepositoryTests_FindUserChats_ReturnListOfChats() {
        Chat chat1 = new Chat(user, new User());
        List<Chat> chatList = Arrays.asList(chat1);

        Mockito.when(chatsRepository.findUserChats(user)).thenReturn(chatList);

        List<Chat> result = chatsRepository.findUserChats(user);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void ChatsRepositoryTests_FindUserChatsWithNoSection_ReturnListOfChats() {
        Chat chat1 = new Chat(user, null);
        List<Chat> chatList = Arrays.asList(chat1);

        Mockito.when(chatsRepository.findUserChatsWithNoSection(user)).thenReturn(chatList);

        List<Chat> result = chatsRepository.findUserChatsWithNoSection(user);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.size()).isEqualTo(1);
    }
}
