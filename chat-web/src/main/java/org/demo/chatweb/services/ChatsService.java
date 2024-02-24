package org.demo.chatweb.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletException;
import org.demo.chatweb.models.Chat;
import org.demo.chatweb.models.Section;
import org.demo.chatweb.models.User;
import org.demo.chatweb.repository.ChatsRepository;
import org.demo.chatweb.repository.MessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ChatsService {
    private final ChatsRepository chatsRepository;
    private final MessagesRepository messagesRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ChatsService(ChatsRepository chatsRepository, MessagesRepository messagesRepository) {
        this.chatsRepository = chatsRepository;
        this.messagesRepository = messagesRepository;
    }

    @Transactional
    public void save(Chat chat)
    {
        chatsRepository.save(chat);
    }
    public List<Chat> index()
    {
        return chatsRepository.findAll();
    }
    @Transactional
    public void removeChat(User user1, User user2) {
        List<Chat> existingChats = chatsRepository.findExistingChats(user1, user2);

        if (!existingChats.isEmpty()) {
            Chat chatToRemove = existingChats.get(0);
            chatsRepository.delete(chatToRemove);
        } else {
            throw new RuntimeException("Invalid chat data");
        }
    }
    @Transactional
    public void createChat(User user1, User user2) throws ServletException {

        if (findExistingChat(user1, user2) == null ) {
            throw new ServletException("Relation already exist");
        }


          Chat chat1 = new Chat();
          chat1.setUser1(user1);
          chat1.setUser2(user2);
          chat1.setCreatedAt(new Date());
          chatsRepository.save(chat1);



        Chat chat2 = new Chat();
        chat2.setUser1(user2);
        chat2.setUser2(user1);
        chat2.setCreatedAt(new Date());
        chatsRepository.save(chat2);
    }


    public List<Chat> findUserChats(User user)
    {
        return chatsRepository.findUserChats(user);
    }



    public List<Chat> findExistingChat(User user1, User user2) {
        List<Chat> chats = chatsRepository.findExistingChats(user1, user2);
        return chats != null ? chats : Collections.emptyList();
    }

    public List<Section> findUserSections(User user) {

        String jpql = "SELECT DISTINCT c.section FROM Chat c WHERE c.user1 = :user";
        return entityManager.createQuery(jpql, Section.class)
                .setParameter("user", user)
                .getResultList();
    }
    public List<Chat> findUserChatsWithNoSection(User user)
    {
        return chatsRepository.findUserChatsWithNoSection(user);
    }
    @Transactional
    public void setSection(List<Chat> chats, Section section )
    {
        for (Chat chat : chats)
        {
            chat.setSection(section);
            save(chat);
        }
    }
    @Transactional
    public void removeSection(List<Chat> chats)
    {
        for (Chat chat : chats)
        {
            chat.setSection(null);
            save(chat);
        }
    }


    @Transactional
    public void deleteAllUsersChats(User user)
    {
        chatsRepository.deleteUserAllChats(user);
    }
}
