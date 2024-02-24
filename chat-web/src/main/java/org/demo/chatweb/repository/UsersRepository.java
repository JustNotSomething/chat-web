package org.demo.chatweb.repository;

import org.demo.chatweb.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<User, Integer> {
    Optional <User> findByUsername(String username);
    List<User> findByUsernameIn(List<String> usernames);

}
