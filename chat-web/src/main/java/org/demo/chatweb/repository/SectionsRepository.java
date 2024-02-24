package org.demo.chatweb.repository;

import org.demo.chatweb.models.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SectionsRepository extends JpaRepository<Section, Integer> {
    Optional<Section> findByTitle(String title);
}
