package org.demo.chatweb.dto;

public class TextMessagesDTO {

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public TextMessagesDTO(){}
    public TextMessagesDTO(String name)
    {
        this.message = name;
    }

}