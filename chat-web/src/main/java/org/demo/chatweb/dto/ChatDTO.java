package org.demo.chatweb.dto;

public class ChatDTO {
    private String senderUsername;
    private String receiverUsername;
    public ChatDTO(){}

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

    @Override
    public String toString() {
        return "ChatDTO{" +
                "senderUsername='" + senderUsername + '\'' +
                ", receiverUsername='" + receiverUsername + '\'' +
                '}';
    }
}
