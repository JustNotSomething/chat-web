package org.demo.chatweb.repository;

import org.demo.chatweb.models.Chat;
import org.demo.chatweb.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatsRepository extends JpaRepository<Chat,Integer> {


    @Query("SELECT f FROM Chat f WHERE " +
            "(f.user1 = :user1 AND f.user2 = :user2) OR " +
            "(f.user1 = :user2 AND f.user2 = :user1)")
    List<Chat> findExistingChats(
            @Param("user1") User user1,
            @Param("user2") User user2
    );

    @Query("SELECT f FROM Chat f WHERE " +
            "(f.user1 = :user) OR " + "(f.user2 = :user)")
    List<Chat> findUserAllChats(@Param("user") User user);


    @Query("DELETE FROM Chat f WHERE " +
            "(f.user1 = :user) OR " + "(f.user2 = :user)")
    void deleteUserAllChats(@Param("user") User user);

    @Query("SELECT f FROM Chat f WHERE " +
            "(f.user1 = :user)")
    List<Chat> findUserChats(@Param("user") User user);


    @Query("SELECT f FROM Chat f WHERE " +
            "(f.user1 = :user) AND (f.section IS NULL) ")
    List<Chat> findUserChatsWithNoSection(@Param("user") User user);

}
