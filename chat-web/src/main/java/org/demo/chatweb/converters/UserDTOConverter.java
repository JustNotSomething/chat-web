package org.demo.chatweb.converters;

import org.demo.chatweb.dto.UserDTO;
import org.demo.chatweb.dto.UserMinDTO;
import org.demo.chatweb.models.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDTOConverter {
    private final ModelMapper modelMapper;

    @Autowired
    public UserDTOConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }


    public UserDTO convertToUserDTO(User user)
    {
        return modelMapper.map(user, UserDTO.class);
    }
    public UserMinDTO convertToMinUserDTO(UserDTO userDTO)
    {
        return modelMapper.map(userDTO, UserMinDTO.class);
    }



}
