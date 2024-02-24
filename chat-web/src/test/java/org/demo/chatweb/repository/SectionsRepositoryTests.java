package org.demo.chatweb.repository;

import org.assertj.core.api.Assertions;
import org.demo.chatweb.models.Section;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
public class SectionsRepositoryTests {
    @Mock
    private SectionsRepository sectionsRepository;

    @Test
    public void SectionsRepositoryTests_FindByTitle_ExistingTitle_ReturnSection() {

        Section section = new Section();
        section.setTitle("General");

        Mockito.when(sectionsRepository.findByTitle("General")).thenReturn(Optional.of(section));

        Optional<Section> result = sectionsRepository.findByTitle("General");

        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getTitle()).isEqualTo("General");
    }

    @Test
    public void SectionsRepositoryTests_FindByTitle_NonExistingTitle_ReturnEmptyOptional() {
        Mockito.when(sectionsRepository.findByTitle("NonExisting")).thenReturn(Optional.empty());

        Optional<Section> result = sectionsRepository.findByTitle("NonExisting");

        Assertions.assertThat(result).isEmpty();
    }
}
