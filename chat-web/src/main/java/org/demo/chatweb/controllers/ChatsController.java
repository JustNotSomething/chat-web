package org.demo.chatweb.controllers;

import jakarta.servlet.ServletException;

import org.demo.chatweb.config.JWTUtil;
import org.demo.chatweb.converters.ChatConverter;
import org.demo.chatweb.dto.AvatarInstDTO;
import org.demo.chatweb.converters.ListMessageDTOConverter;
import org.demo.chatweb.dto.MessageDTO;
import org.demo.chatweb.dto.UserDTO;
import org.demo.chatweb.models.Chat;
import org.demo.chatweb.models.Message;
import org.demo.chatweb.models.User;
import org.demo.chatweb.services.ChatsService;
import org.demo.chatweb.services.MessagesService;
import org.demo.chatweb.services.StorageService;
import org.demo.chatweb.services.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chats")
public class ChatsController {

    private final ChatsService chatsService;
    private final JWTUtil jwtUtil;

    private final UsersService usersService;
    private final ChatConverter chatConverter;
    private final MessagesService messagesService;
    private final ListMessageDTOConverter listMessageDTOConverter;
    private final StorageService storageService;
    private static final Logger logger = LoggerFactory.getLogger(ChatsController.class);;

    @Autowired
    public ChatsController(ChatsService chatsService, JWTUtil jwtUtil, UsersService usersService, ChatConverter chatConverter, MessagesService messagesService, ListMessageDTOConverter listMessageDTOConverter, StorageService storageService) {
        this.chatsService = chatsService;
        this.jwtUtil = jwtUtil;
        this.usersService = usersService;
        this.chatConverter = chatConverter;
        this.messagesService = messagesService;
        this.listMessageDTOConverter = listMessageDTOConverter;
        this.storageService = storageService;
    }

    @GetMapping
    ResponseEntity<List<UserDTO>> getFriends(@RequestHeader(value = "Authorization") String authHeader) throws ServletException {
        String username = jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader);
        User user = usersService.getByUsername(username);
        List <Chat> chats = chatsService.findUserChats(user);
        List<UserDTO> userChats = chatConverter.convertToChatList(user, chats);

        logger.info("Fetch user - " + username + " friends");

        return new ResponseEntity<>(userChats, HttpStatus.OK);
    }

    @GetMapping("/avatars")
    private ResponseEntity<List<AvatarInstDTO>> getFriendsAvatars(@RequestHeader(value = "Authorization") String authHeader) throws ServletException, IOException {
        String username = jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader);
        User user = usersService.getByUsername(username);
        List <Chat> chats = chatsService.findUserChats(user);
        List<User> userChats = chatConverter.convertToChatListUser(user, chats);


        List<AvatarInstDTO> allAvatars = new ArrayList<>();
        for (User someUser : userChats)
        {
            if (someUser.getAvatar() != null)
            {
                System.out.println(someUser);

                AvatarInstDTO avatarInstDTO = new AvatarInstDTO();

                avatarInstDTO.setId(someUser.getId());

                String filePath = "avatars/" + someUser.getAvatar();
                byte[] data = storageService.downloadFile(filePath);
                avatarInstDTO.setAvatarData(data);

                allAvatars.add(avatarInstDTO);

            }
        }
        return ResponseEntity.ok(allAvatars);

    }

    @GetMapping("/loadHistory")
    List<MessageDTO>  getUserChatMessages(@RequestParam String senderUsername,
                                          @RequestParam String receiverUsername)
    {

        String SenderUsername = senderUsername;
        String ReceiverUsername = receiverUsername;

        List<Chat> existingChats = chatsService.findExistingChat(usersService.getByUsername(SenderUsername), usersService.getByUsername(ReceiverUsername));
        Chat senderChat = findSenderChar(existingChats, usersService.getByUsername(SenderUsername));
        List<Message> senderMessages = messagesService.getChatMessages(senderChat);
        List<MessageDTO> msgS = listMessageDTOConverter.convertList(senderMessages);

        logger.info("Load " + SenderUsername + " & " + ReceiverUsername + " chat history");

        return msgS;
    }

    private Chat findSenderChar(List<Chat> chats, User senderUser)
    {
        if (chats.get(0).getUser1() == senderUser)
        {
            return chats.get(0);
        }
        else
        {
            return chats.get(1);
        }
    }



    @GetMapping("/getLastMessages")
    List<MessageDTO> getLastMessages(@RequestHeader(value = "Authorization") String authHeader) throws ServletException {
        String username = jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader);
        User user = usersService.getByUsername(username);

        List<MessageDTO> chat_msg = listMessageDTOConverter.convertList(messagesService.findLastMessagesForChats());


        List<MessageDTO> uniqueLastChatMessages = chat_msg.stream()
                .collect(Collectors.toMap(
                        msg -> msg.getSenderUsername() + "-" + msg.getReceiverUsername(),
                        msg -> msg,
                        (existing, replacement) -> existing
                ))
                .values()
                .stream()
                .collect(Collectors.toList());
        System.out.println(uniqueLastChatMessages);
        return uniqueLastChatMessages;


    }
}
