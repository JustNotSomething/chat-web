package org.demo.chatweb.converters;

import org.demo.chatweb.dto.MessageDTO;
import org.demo.chatweb.dto.UserDTO;
import org.demo.chatweb.models.Chat;
import org.demo.chatweb.models.Message;
import org.demo.chatweb.models.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ListMessageDTOConverter {

    private final ModelMapper modelMapper;
    private final MessageDTOConverter messageDTOConverter;
    @Autowired
    public ListMessageDTOConverter(ModelMapper modelMapper, MessageDTOConverter messageDTOConverter) {
        this.modelMapper = modelMapper;
        this.messageDTOConverter = messageDTOConverter;
    }

    public List<MessageDTO> convertList(List<Message> msgList)
    {
       List<MessageDTO> msgDTO = msgList.stream().map(messageDTOConverter::convertToMessageDTO).collect(Collectors.toList());


        return msgDTO;
    }
}
