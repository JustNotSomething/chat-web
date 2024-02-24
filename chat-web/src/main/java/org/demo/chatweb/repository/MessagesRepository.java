package org.demo.chatweb.repository;

import org.demo.chatweb.models.Message;
import org.demo.chatweb.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessagesRepository extends JpaRepository<Message, Integer> {
    List<Message> findAllByChat_ChatId(int chatId);

    @Modifying
    @Query("DELETE FROM Message f WHERE " +
            "(f.sender = :user) OR " + "(f.receiver = :user)")
    void deleteUserAllMessages(@Param("user") User user);
}