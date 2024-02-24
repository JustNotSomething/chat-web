package org.demo.chatweb.converters;

import org.demo.chatweb.dto.SectionDTO;
import org.demo.chatweb.models.Chat;
import org.demo.chatweb.models.Section;
import org.demo.chatweb.models.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SectionDTOConverter {
    public List<SectionDTO> convertToDTOList(List<Section> sections, User user) {
        List<SectionDTO> sectionDTOList = new ArrayList<>();

        for (Section section : sections) {
            List<String> usernames = new ArrayList<>();


            for (Chat chat : section.getChats()) {
                if (chat.getUser2() != null && !usernames.contains(chat.getUser2().getUsername()) && chat.getUser1() == user) {
                    usernames.add(chat.getUser2().getUsername());
                }
            }

            SectionDTO sectionDTO = new SectionDTO(section.getTitle(), usernames);
            sectionDTOList.add(sectionDTO);
        }

        return sectionDTOList;
    }

}
