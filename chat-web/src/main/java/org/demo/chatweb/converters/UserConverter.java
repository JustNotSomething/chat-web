package org.demo.chatweb.converters;

import org.demo.chatweb.dto.UserDTO;
import org.demo.chatweb.dto.UserMinDTO;
import org.demo.chatweb.models.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    private final ModelMapper modelMapper;

    @Autowired
    public UserConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public User convertToUser(UserDTO userDTO)
    {
        return modelMapper.map(userDTO, User.class);
    }

    public UserMinDTO convertToUserMinDTO(User user)
    {
        return modelMapper.map(user, UserMinDTO.class);
    }
}
