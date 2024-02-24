package org.demo.chatweb.services;

import org.demo.chatweb.models.Section;
import org.demo.chatweb.repository.SectionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SectionsService {
    private final SectionsRepository sectionsRepository;

    @Autowired
    public SectionsService(SectionsRepository sectionsRepository) {
        this.sectionsRepository = sectionsRepository;
    }

    public void save(String title)
    {
        Section section = new Section();
        section.setTitle(title);
        sectionsRepository.save(section);
    }

    public void save(Section section)
    {
        sectionsRepository.save(section);
    }

    public Section getByTitle(String title)
    {
        Optional<Section> section = sectionsRepository.findByTitle(title);
        return section.get();
    }

}
