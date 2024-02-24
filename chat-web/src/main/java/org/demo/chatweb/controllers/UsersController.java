package org.demo.chatweb.controllers;

import jakarta.servlet.ServletException;
import org.demo.chatweb.config.JWTUtil;
import org.demo.chatweb.converters.ChatConverter;
import org.demo.chatweb.converters.UserConverter;
import org.demo.chatweb.converters.UserDTOConverter;
import org.demo.chatweb.dto.AvatarInstDTO;
import org.demo.chatweb.dto.UserDTO;
import org.demo.chatweb.dto.VisibilityDTO;
import org.demo.chatweb.models.Chat;
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
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UsersController {
    private final ChatsService chatsService;
    private final UsersService usersService;
    private final JWTUtil jwtUtil;
    private final ChatConverter chatConverter;
    private final UserDTOConverter userDTOConverter;
    private final UserConverter userConverter;
    private final StorageService storageService;
    private final MessagesService messagesService;
    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    public UsersController(ChatsService chatsService, UsersService usersService, JWTUtil jwtUtil, ChatConverter chatConverter, UserDTOConverter userDTOConverter, UserConverter userConverter, StorageService storageService, MessagesService messagesService) {
        this.chatsService = chatsService;
        this.usersService = usersService;
        this.jwtUtil = jwtUtil;
        this.chatConverter = chatConverter;
        this.userDTOConverter = userDTOConverter;
        this.userConverter = userConverter;
        this.storageService = storageService;
        this.messagesService = messagesService;
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<UserDTO>> getAllUnknownUsers(@RequestHeader(value = "Authorization") String authHeader) throws ServletException, AccessDeniedException {
        try {
            String username = jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader);
            User user = usersService.getByUsername(username);



            List<UserDTO> users = new ArrayList<>();
            if (user.getRole().equals("ROLE_ADMIN")) {
                users = usersService.index().stream()
                        .filter(user1 -> !user1.getRole().contains("ROLE_ADMIN"))
                        .map(userDTOConverter::convertToUserDTO)
                        .collect(Collectors.toList());

            } else {
                logger.warn("Unauthorized access");
                throw new AccessDeniedException("Unauthorized access");
            }

            logger.info("Admin - " + user.getUsername() + " fetched all users");
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("/getAllUsers/avatars")
    ResponseEntity<List<AvatarInstDTO>> getAllUnknownUsersAvatars(@RequestHeader(value = "Authorization") String authHeader) throws ServletException, AccessDeniedException {
        try {
            String username = jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader);
            User user = usersService.getByUsername(username);



            List<User> users = new ArrayList<>();
            if (user.getRole().equals("ROLE_ADMIN")) {
                users = usersService.index().stream()
                        .filter(user1 -> !user1.getRole().contains("ROLE_ADMIN"))
                        .collect(Collectors.toList());
            }
            else {
                throw new AccessDeniedException("Unauthorized access");
            }

            List<AvatarInstDTO> allAvatars = new ArrayList<>();
            for (User someUser : users)
            {
                if (someUser.getAvatar() != null)
                {
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
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }








    @GetMapping("/getUsers/avatars")
    private ResponseEntity<List<AvatarInstDTO>> getUnknownUsersAvatars(@RequestHeader(value = "Authorization") String authHeader) throws ServletException, IOException {
        String username = jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader);
        User user = usersService.getByUsername(username);


        List <Chat> chats = chatsService.findUserChats(user);
        List<UserDTO> userChats = chatConverter.convertToChatList(user, chats);

        List<User> allUsers = usersService.index()
                .stream()
                .filter(userInst -> !userInst.getHideProfile())
                .filter(userInst -> !isUserHaveChat(userInst, userChats))
                .filter(userDTO -> !username.equals(userDTO.getUsername()))
                .collect(Collectors.toList());

        List<AvatarInstDTO> allAvatars = new ArrayList<>();
        for (User someUser : allUsers)
        {
            if (someUser.getAvatar() != null)
            {

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



    @GetMapping("/getUsers")
    ResponseEntity<List<UserDTO>> getUnknownUsers(@RequestHeader(value = "Authorization") String authHeader) throws ServletException, IOException {
        String username = jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader);
        User user = usersService.getByUsername(username);


        List <Chat> chats = chatsService.findUserChats(user);
        List<UserDTO> userChats = chatConverter.convertToChatList(user, chats);

        List<UserDTO> allUsers = usersService.index()
                .stream()
                .filter(userInst -> !userInst.getHideProfile())
                .map(userDTOConverter::convertToUserDTO)
                .filter(userDTO -> !isUserHaveChat(userDTO, userChats))
                .filter(userDTO -> !username.equals(userDTO.getUsername()))
                .collect(Collectors.toList());

        logger.info("User - " + user.getUsername() + " fetched other users");

        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }


    @PostMapping("/saveUser/{id}")
    ResponseEntity addFriendRelation(@RequestHeader(value = "Authorization") String authHeader, @PathVariable("id") Integer id) throws ServletException
    {
        String username = jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader);
        User user1 = usersService.getByUsername(username);
        User user2 = usersService.find(id);
        chatsService.createChat(user1, user2);

        logger.info("New relation was created: " + user1.getUsername() + " & " + user2.getUsername());

        return new ResponseEntity(HttpStatus.OK);
    }


    private boolean isUserHaveChat(UserDTO user, List<UserDTO> friends) {
        return friends.stream().anyMatch(friend -> friend.getUsername().equals(user.getUsername()));
    }

    private boolean isUserHaveChat(User user, List<UserDTO> friends) {
        return friends.stream().anyMatch(friend -> friend.getUsername().equals(user.getUsername()));
    }

    @PostMapping("/deleteUser/{id}")
    ResponseEntity removeRelation(@RequestHeader(value = "Authorization") String authHeader, @PathVariable("id") Integer id) throws ServletException
    {
        String username = jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader);
        User user1 = usersService.getByUsername(username);
        User user2 = usersService.find(id);

        chatsService.removeChat(user1, user2);
        chatsService.removeChat(user2, user1);

        logger.info("Relation was deleted: " + user1.getUsername() + " & " + user2.getUsername());


        return new ResponseEntity(HttpStatus.OK);
    }


    @DeleteMapping("/deleteProfile")
    public ResponseEntity<HttpStatus> deleteUser(@RequestHeader(value = "Authorization") String authHeader)
    {
        try{
            String username = jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader);
            User user = usersService.getByUsername(username);
            usersService.delete(user);
            return new ResponseEntity<>(HttpStatus.OK);

        }catch (Exception e)
        {
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }

    }
    @PatchMapping("/updateUser")
    public Map<String, String> updateUserProfile(@RequestHeader(value = "Authorization") String authHeader, @RequestBody UserDTO userDTO) throws ServletException {
        try {
            String username = jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader);
            User user = usersService.getByUsername(username);
            User prevUser = user;

            if (user == null) {
                return Map.of("message", "error"); // User not found
            }

            User userToUpdate = userConverter.convertToUser(userDTO);

            user.setPassword(userToUpdate.getPassword());
            user.setUsername(userToUpdate.getUsername());
            user.setEmail(userToUpdate.getEmail());
            user.setDateOfBirth(userToUpdate.getDateOfBirth());
            user.setPhone(userToUpdate.getPhone());

            usersService.save(user);

            String token = jwtUtil.generateToken(user.getUsername(), user.getRole(), user.getEmail());


            logger.info("User updated his profile data: Previous user data" + prevUser + " -> " + user);
            logger.info("Updated user jwt-token - " + user + ": " + token);

            return Map.of("jwt-token", token);

        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("Error while updating a user account");
            return Map.of("message", "error");
        }
    }



    @PatchMapping("/hideProfile")
    public ResponseEntity<HttpStatus> hideProfile(@RequestHeader(value = "Authorization") String authHeader)
    {

        try{
            String username = jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader);
            User user = usersService.getByUsername(username);

            logger.info("User - " + username + " hid his profile");

            usersService.saveAndHide(user, true);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e)
        {
            logger.warn("Error while hiding a user profile");

            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }

    }
    @PatchMapping("/revealProfile")
    public ResponseEntity<HttpStatus> revealProfile(@RequestHeader(value = "Authorization") String authHeader)
    {

        try{
            String username = jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader);
            User user = usersService.getByUsername(username);

            usersService.saveAndHide(user, false);


            logger.info("User - " + username + " opened his profile");
            return new ResponseEntity<>(HttpStatus.OK);

        }catch (Exception e)
        {
            logger.warn("Error while revealing a user's profile");
            return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
        }

    }


    @GetMapping("/getProfileVisibility")
    public ResponseEntity<VisibilityDTO> isVisible(@RequestHeader(value = "Authorization") String authHeader) throws ServletException {
         String username = jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader);
            User user = usersService.getByUsername(username);
            VisibilityDTO visibilityDTO = new VisibilityDTO();
            if (user.getHideProfile()) {
                visibilityDTO.setVisible(true);
            }
            else {
                visibilityDTO.setVisible(false);
            }
            return new ResponseEntity<>(visibilityDTO, HttpStatus.OK);
    }


    @PostMapping("/deleteUserAccount/{id}")
    ResponseEntity deleteUserAccount(@RequestHeader(value = "Authorization") String authHeader, @PathVariable("id") Integer id) throws ServletException
    {
        try{

            String username = jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader);
            User admin = usersService.getByUsername(username);

            if (admin.getRole().equals("ROLE_ADMIN"))
            {
                User user = usersService.find(id);

                messagesService.deleteAllUsersMessages(user);
                usersService.delete(user);


                logger.info("Admin - " + username + " deleted user account: " + user.getUsername());
            }

            else {
                logger.warn("Unauthorized access | Attempting to delete a user");
                throw new AccessDeniedException("Unauthorized access");
            }

            return new ResponseEntity(HttpStatus.OK);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/changeUserRole/{id}")
    ResponseEntity changeUserRole(@RequestHeader(value = "Authorization") String authHeader, @PathVariable("id") Integer id) throws ServletException
    {
        try{

            String username = jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader);
            User admin = usersService.getByUsername(username);
            if (admin.getRole().equals("ROLE_ADMIN"))
            {
                User user = usersService.find(id);
                usersService.giveAdminRights(user);

                logger.info("Admin - " + username + " granted administrator rights to: " + user.getUsername());
            }

            else {

                logger.warn("Unauthorized access | Attempting to change user role");
                throw new AccessDeniedException("Unauthorized access");
            }

            return new ResponseEntity(HttpStatus.OK);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
