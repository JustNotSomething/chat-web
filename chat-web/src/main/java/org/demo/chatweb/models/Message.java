package org.demo.chatweb.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import javax.xml.crypto.Data;
import java.util.Date;

@Entity
@Table(name = "Message")
public class Message {
    @Id
    @Column(name = "message_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int messageId;
    @ManyToOne
    @JoinColumn(name = "chat_id", nullable = false)
    @NotNull
    private Chat chat;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    @NotNull
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    @NotNull
    private User receiver;

    @Column(name = "message_text")
    @NotBlank
    private String messageText;
    @Column(name = "sent_at")
    @NotNull
    private Date sentAt;

    public Message(){}

    public Message(Chat chat, User sender, User receiver, String messageText, Date sentAt) {
        this.chat = chat;
        this.sender = sender;
        this.receiver = receiver;
        this.messageText = messageText;
        this.sentAt = sentAt;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }


    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public Date getSentAt() {
        return sentAt;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", chat=" + chat +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", messageText='" + messageText + '\'' +
                ", sentAt=" + sentAt +
                '}';
    }
}
