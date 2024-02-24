package org.demo.chatweb.services;

import org.demo.chatweb.models.Section;
import org.demo.chatweb.repository.SectionsRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SectionsServiceTests {

    @Mock
    private SectionsRepository sectionsRepository;

    @InjectMocks
    private SectionsService sectionsService;

    @Test
    public void SectionsService_SaveWithTitle_CallsRepositorySave() {
        String title = "Test Section";

        sectionsService.save(title);

        verify(sectionsRepository, Mockito.times(1)).save(Mockito.any(Section.class));
    }

    @Test
    public void SectionsService_SaveWithSection_CallsRepositorySave() {
        Section section = new Section();
        section.setTitle("Test Section");

        sectionsService.save(section);

        verify(sectionsRepository, Mockito.times(1)).save(section);
    }

    @Test
    public void SectionsService_GetByTitle_Section() {
        String title = "Test Section";
        Section section = new Section();
        section.setTitle(title);

        when(sectionsRepository.findByTitle(title)).thenReturn(Optional.of(section));

        Section result = sectionsService.getByTitle(title);

        assertEquals(section, result);
    }



}
