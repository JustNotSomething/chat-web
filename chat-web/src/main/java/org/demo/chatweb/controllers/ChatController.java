package org.demo.chatweb.controllers;

import org.demo.chatweb.dto.MessageDTO;
import org.demo.chatweb.models.Chat;
import org.demo.chatweb.models.Message;
import org.demo.chatweb.services.ChatsService;
import org.demo.chatweb.services.MessagesService;
import org.demo.chatweb.services.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class ChatController {

    private final UsersService usersService;
    private final ChatsService chatsService;
    private final MessagesService messagesService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);;


    @Autowired
    public ChatController(UsersService usersService, ChatsService chatsService, MessagesService messagesService, SimpMessagingTemplate simpMessagingTemplate) {
        this.usersService = usersService;
        this.chatsService = chatsService;
        this.messagesService = messagesService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/send-message/{receiverUsername}")

    public void sendMessage(@Payload MessageDTO payload, @DestinationVariable String receiverUsername) {
        String senderUsername = payload.getSenderUsername();
        String message = payload.getMessage();
        System.out.println(payload);
        List<Chat> existingChats = chatsService.findExistingChat(usersService.getByUsername(senderUsername), usersService.getByUsername(receiverUsername));

        Message messageToSave1 = new Message();
        messageToSave1.setChat(existingChats.get(0));
        messageToSave1.setSender(usersService.getByUsername(senderUsername));
        messageToSave1.setReceiver(usersService.getByUsername(receiverUsername));
        messageToSave1.setMessageText(message);
        messageToSave1.setSentAt(new Date());
        messagesService.save(messageToSave1);


        Message messageToSave2 = new Message();
        messageToSave2.setChat(existingChats.get(1));
        messageToSave2.setSender(usersService.getByUsername(senderUsername));
        messageToSave2.setReceiver(usersService.getByUsername(receiverUsername));
        messageToSave2.setMessageText(message);
        messageToSave2.setSentAt(new Date());
        messagesService.save(messageToSave2);

        String destination = "/user/" + receiverUsername + "/message";
        //        simpMessagingTemplate.convertAndSend(destination, message);

        logger.info("Message: " + payload.getMessage() + " | " + payload.getSenderUsername() + " -> " + payload.getReceiverUsername());

        simpMessagingTemplate.convertAndSend(destination, payload);


    }

}




