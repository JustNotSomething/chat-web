package org.demo.chatweb.converters;

import org.demo.chatweb.dto.MessageDTO;
import org.demo.chatweb.models.Message;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageDTOConverter {
    private final ModelMapper modelMapper;
    @Autowired
    public MessageDTOConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public MessageDTO convertToMessageDTO(Message message)
    {
        return modelMapper.map(message, MessageDTO.class);
    }
    public Message convertToMessage(MessageDTO messageDTO)
    {
        return modelMapper.map(messageDTO, Message.class);
    }

}
