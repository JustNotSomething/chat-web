package org.demo.chatweb.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private int chatId;

    @ManyToOne
    @JoinColumn(name = "user_id1" )
    @NotNull
    private User user1;

    @ManyToOne
    @JoinColumn(name = "user_id2")
    @NotNull
    private User user2;

    @Column(name = "created_at")
    @NotNull
    private Date createdAt;
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages;

    @ManyToOne
    @JoinColumn(name = "section_id")

    private Section section;


    public Chat(){}

    public Chat( Date createdAt) {

        this.createdAt = createdAt;
    }

    public Chat(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }





    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }


    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "chatId=" + chatId +
                ", user1=" + user1 +
                ", user2=" + user2 +
                ", createdAt=" + createdAt +
                ", messages=" + messages +
                ", section=" + (section != null ? section.getId() : null) + // Reference section by its ID, not the entire object
                '}';
    }
}
