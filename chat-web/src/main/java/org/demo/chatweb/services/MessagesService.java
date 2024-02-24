package org.demo.chatweb.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.demo.chatweb.models.Chat;
import org.demo.chatweb.models.Message;
import org.demo.chatweb.models.User;
import org.demo.chatweb.repository.MessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessagesService {
    private final MessagesRepository messagesRepository;

    @Autowired
    public MessagesService(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    public void save(Message message)
    {
        messagesRepository.save(message);
    }

    public List<Message> getChatMessages(Chat chat)
    {
        return messagesRepository.findAllByChat_ChatId(chat.getChatId());
    }

    @Transactional
    public void deleteAllUsersMessages(User user)
    {
        messagesRepository.deleteUserAllMessages(user);
    }



    @PersistenceContext
    private EntityManager entityManager;

    public List<Message> findLastMessagesForChats() {
        String jpql = "SELECT m FROM Message m " +
                "WHERE m.sentAt = (SELECT MAX(m2.sentAt) FROM Message m2 WHERE m2.chat = m.chat)";
        return entityManager.createQuery(jpql, Message.class)
                .getResultList();
    }


}
