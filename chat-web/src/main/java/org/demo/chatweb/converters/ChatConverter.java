package org.demo.chatweb.converters;

import org.demo.chatweb.dto.UserDTO;
import org.demo.chatweb.dto.UserMinDTO;
import org.demo.chatweb.models.Chat;
import org.demo.chatweb.models.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ChatConverter {
    private final ModelMapper modelMapper;
    private final UserDTOConverter userDTOConverter;
    private final UserConverter userConverter;
    @Autowired
    public ChatConverter(ModelMapper modelMapper, UserDTOConverter userDTOConverter, UserConverter userConverter) {
        this.modelMapper = modelMapper;
        this.userDTOConverter = userDTOConverter;
        this.userConverter = userConverter;
    }

    public List<UserDTO> convertToChatList(User user, List<Chat> userChats)
    {
        List <User> chats = new ArrayList<>();
        for (Chat f : userChats)
        {
            chats.add(f.getUser2());
        }

        List<UserDTO> chatRelations = chats.stream()
                .map(userDTOConverter::convertToUserDTO)
                .collect(Collectors.toList());

        return chatRelations;
    }

    public List<User> convertToChatListUser(User user, List<Chat> userChats)
    {
        List <User> chats = new ArrayList<>();
        for (Chat f : userChats)
        {
            chats.add(f.getUser2());
        }

        return chats;
    }

    public List<UserMinDTO> convertToMinDTOList(List<Chat> chats) {
        Set<UserMinDTO> uniqueUsers = new HashSet<>();

        for (Chat chat : chats) {
            UserMinDTO userMinDTO = userConverter.convertToUserMinDTO(chat.getUser2());
            uniqueUsers.add(userMinDTO);
        }
        List<UserMinDTO> sortedUsers = uniqueUsers.stream()
                .sorted(Comparator.comparing(UserMinDTO::getUsername))
                .collect(Collectors.toList());

        return sortedUsers;
    }

}
