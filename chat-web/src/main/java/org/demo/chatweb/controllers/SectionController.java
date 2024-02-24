package org.demo.chatweb.controllers;

import jakarta.servlet.ServletException;
import org.demo.chatweb.config.JWTUtil;
import org.demo.chatweb.converters.ChatConverter;
import org.demo.chatweb.converters.SectionDTOConverter;
import org.demo.chatweb.dto.*;
import org.demo.chatweb.models.Chat;
import org.demo.chatweb.models.Section;
import org.demo.chatweb.models.User;
import org.demo.chatweb.services.ChatsService;
import org.demo.chatweb.services.SectionsService;
import org.demo.chatweb.services.StorageService;
import org.demo.chatweb.services.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/sections")
public class SectionController {
    private final SectionsService sectionsService;
    private final JWTUtil jwtUtil;
    private final UsersService usersService;
    private final ChatsService chatsService;
    private final SectionDTOConverter sectionDTOConverter;
    private final ChatConverter chatConverter;
    private final StorageService storageService;
    private static final Logger logger = LoggerFactory.getLogger(SectionController.class);

    @Autowired
    public SectionController(SectionsService sectionsService, JWTUtil jwtUtil, UsersService usersService, ChatsService chatsService, SectionDTOConverter sectionDTOConverter, ChatConverter chatConverter, StorageService storageService) {
        this.sectionsService = sectionsService;
        this.jwtUtil = jwtUtil;
        this.usersService = usersService;
        this.chatsService = chatsService;
        this.sectionDTOConverter = sectionDTOConverter;
        this.chatConverter = chatConverter;
        this.storageService = storageService;
    }

    @GetMapping
    public ResponseEntity<List<SectionDTO>> getUserSections(@RequestHeader(value = "Authorization") String authHeader) throws ServletException {
        try {
            String username = jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader);
            User user = usersService.getByUsername(username);
            List<Section> userSections = chatsService.findUserSections(user);

            List<SectionDTO> sectionDTOs = sectionDTOConverter.convertToDTOList(userSections, user);

            logger.info("Load user - " + username + " sections");

            return ResponseEntity.ok(sectionDTOs);
        } catch (Exception e) {

            logger.warn("Sections loading error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }


    /////
    @GetMapping("/getOther")
    public ResponseEntity<List<UserMinDTO>> getUsersWithoutSection(@RequestHeader(value = "Authorization") String authHeader) throws ServletException {
        String username = jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader);
        User user = usersService.getByUsername(username);
        List<Chat> noSectionChats = chatsService.findUserChatsWithNoSection(user);
        List <UserMinDTO> users = chatConverter.convertToMinDTOList(noSectionChats);
        return  ResponseEntity.ok(users);
    }


    @GetMapping("/getOther/avatars")
    public ResponseEntity<List<AvatarInstDTO>> getUsersWithoutSectionAvatars(@RequestHeader(value = "Authorization") String authHeader) throws ServletException, IOException {
        String username = jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader);
        User user = usersService.getByUsername(username);
        List<Chat> noSectionChats = chatsService.findUserChatsWithNoSection(user);
        List <User> users = chatConverter.convertToChatListUser(user, noSectionChats);

        List<AvatarInstDTO> allAvatars = new ArrayList<>();
        for (User someUser : users)
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


        return  ResponseEntity.ok(allAvatars);
    }

    @PostMapping("/create")
    public ResponseEntity<HttpStatus> createSection(@RequestBody SectionDTO sectionDTO, @RequestHeader(value = "Authorization") String authHeader) throws ServletException {
        String username = jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader);
        User user = usersService.getByUsername(username);

        List<User> userFriends = usersService.getByUsernames(sectionDTO.getUsers());

        List<Chat> chats = new ArrayList<>();




        for (User friend : userFriends)
        {
           chats.add(chatsService.findExistingChat(user, friend).stream()
                   .filter(chat -> chat.getUser1().equals(user))
                   .collect(Collectors.toList()).get(0));
        }

        Section section = new Section();
        section.setTitle(sectionDTO.getTitle());
        section.setChats(chats);
        sectionsService.save(section);
        chatsService.setSection(chats, section);

        logger.info("User - " + username + " created new section - " + sectionDTO.getTitle());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<HttpStatus> updateSection(@RequestBody UpdateSectionDTO updateSectionDTO, @RequestHeader(value = "Authorization") String authHeader) throws ServletException {
        String username = jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader);
        User user = usersService.getByUsername(username);
        Section section = sectionsService.getByTitle(updateSectionDTO.getPrevTitle());

        if (updateSectionDTO.getNewTitle() == "")
        {
            section.setTitle(updateSectionDTO.getPrevTitle());
        }
        else
        {
            section.setTitle(updateSectionDTO.getNewTitle());
        }


        if (updateSectionDTO.getUsersToAdd().size() > 0)
        {

            List<User> usersToAdd = usersService.getByUsernames(updateSectionDTO.getUsersToAdd());
            List<Chat> chats = new ArrayList<>();

            for (User friend : usersToAdd)
            {
                chats.add(chatsService.findExistingChat(user, friend).stream()
                        .filter(chat -> chat.getUser1().equals(user))
                        .collect(Collectors.toList()).get(0));
            }

            chatsService.setSection(chats, section);
        }

        if (updateSectionDTO.getUsersToDelete().size() > 0)
        {
            List<User> usersToRemove = usersService.getByUsernames(updateSectionDTO.getUsersToDelete());
            List<Chat> chatsToRemove = new ArrayList<>();

            for (User friend : usersToRemove)
            {
                chatsToRemove.add(chatsService.findExistingChat(user, friend).stream()
                        .filter(chat -> chat.getUser1().equals(user))
                        .collect(Collectors.toList()).get(0));
            }

            chatsService.removeSection(chatsToRemove);
        }


        logger.info("User - " + username + " updated Section: " + updateSectionDTO.getPrevTitle() + " -> " + updateSectionDTO.getNewTitle());

        return new ResponseEntity<>(HttpStatus.OK);

    }
}
