package org.demo.chatweb.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MessageDTO {
    @JsonProperty("message")
    private String message;
    @JsonProperty("senderUsername")
    private String senderUsername;
    @JsonProperty("receiverUsername")
    private String receiverUsername;
    @JsonProperty("sentAt")
    private Date sentAt;
    public MessageDTO(){}
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public MessageDTO(String message, String receiverUsername) {
        this.message = message;
        this.receiverUsername = receiverUsername;
    }


    public MessageDTO(String message, String senderUsername, String receiverUsername) {
        this.message = message;
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
    }

    public MessageDTO(String message, String senderUsername, String receiverUsername, Date sentAt) {
        this.message = message;
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.sentAt = sentAt;
    }

    public Date getSentAt() {
        return sentAt;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "message='" + message + '\'' +
                ", senderUsername='" + senderUsername + '\'' +
                ", receiverUsername='" + receiverUsername + '\'' +
                ", sentAt=" + sentAt +
                '}';
    }
}
