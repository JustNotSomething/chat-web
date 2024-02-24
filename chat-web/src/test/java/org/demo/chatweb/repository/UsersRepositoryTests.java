package org.demo.chatweb.repository;

import org.assertj.core.api.Assertions;
import org.demo.chatweb.models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UsersRepositoryTests {
    @Autowired
    private UsersRepository usersRepository;

    @Test
    public void UsersRepository_SaveAll_ReturnSavedUser()
    {
        User user = new User();
        user.setOnline(false);
        user.setRole("ROLE_USER");
        user.setUsername("Jack");
        user.setPhone("+11111111111");
        user.setPassword("1111");
        user.setEmail("some@gmail.com");
        user.setHideProfile(true);
        user.setDateOfBirth(new Date());

        User savedUser = usersRepository.save(user);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void UsersRepository_GetAll_ReturnAllUsers()
    {
        User user1 = new User();
        user1.setOnline(false);
        user1.setRole("ROLE_USER");
        user1.setUsername("Jack");
        user1.setPhone("+11111111111");
        user1.setPassword("1111");
        user1.setEmail("some@gmail.com");
        user1.setHideProfile(true);
        user1.setDateOfBirth(new Date());


        User user2 = new User();
        user2.setOnline(false);
        user2.setRole("ROLE_USER");
        user2.setUsername("Dom");
        user2.setPhone("+11111111111");
        user2.setPassword("1111");
        user2.setEmail("some@gmail.com");
        user2.setHideProfile(true);
        user2.setDateOfBirth(new Date());

        usersRepository.save(user1);
        usersRepository.save(user2);

        List<User> userList = usersRepository.findAll();

        Assertions.assertThat(userList).isNotNull();
        Assertions.assertThat(userList.size()).isEqualTo(2);
    }


    @Test
    public void UsersRepository_FindById_ReturnUser()
    {
        User user = new User();
        user.setOnline(false);
        user.setRole("ROLE_USER");
        user.setUsername("Jack");
        user.setPhone("+11111111111");
        user.setPassword("1111");
        user.setEmail("some@gmail.com");
        user.setHideProfile(true);
        user.setDateOfBirth(new Date());

        usersRepository.save(user);

        User foundUser = usersRepository.findById(user.getId()).get();

        Assertions.assertThat(foundUser).isNotNull();

    }

    @Test
    public void UsersRepository_FindByUsername_ReturnUser()
    {
        User user = new User();
        user.setOnline(false);
        user.setRole("ROLE_USER");
        user.setUsername("Jack");
        user.setPhone("+11111111111");
        user.setPassword("1111");
        user.setEmail("some@gmail.com");
        user.setHideProfile(true);
        user.setDateOfBirth(new Date());

        usersRepository.save(user);

        User foundUser = usersRepository.findByUsername(user.getUsername()).get();

        Assertions.assertThat(foundUser).isNotNull();

    }


    @Test
    public void UsersRepository_FindByManyUsernames_ReturnUsers()
    {
        User user1 = new User();
        user1.setOnline(false);
        user1.setRole("ROLE_USER");
        user1.setUsername("Jack");
        user1.setPhone("+11111111111");
        user1.setPassword("1111");
        user1.setEmail("some@gmail.com");
        user1.setHideProfile(true);
        user1.setDateOfBirth(new Date());

        User user2 = new User();
        user2.setOnline(false);
        user2.setRole("ROLE_USER");
        user2.setUsername("Greg");
        user2.setPhone("+11111111111");
        user2.setPassword("1111");
        user2.setEmail("some@gmail.com");
        user2.setHideProfile(true);
        user2.setDateOfBirth(new Date());


        User user3 = new User();
        user3.setOnline(false);
        user3.setRole("ROLE_USER");
        user3.setUsername("Mary");
        user3.setPhone("+11111111111");
        user3.setPassword("1111");
        user3.setEmail("some@gmail.com");
        user3.setHideProfile(true);
        user3.setDateOfBirth(new Date());


        User user4 = new User();
        user4.setOnline(false);
        user4.setRole("ROLE_USER");
        user4.setUsername("Wane");
        user4.setPhone("+11111111111");
        user4.setPassword("1111");
        user4.setEmail("some@gmail.com");
        user4.setHideProfile(true);
        user4.setDateOfBirth(new Date());

        usersRepository.save(user1);
        usersRepository.save(user2);
        usersRepository.save(user3);
        usersRepository.save(user4);

        List<User> userList = usersRepository.findByUsernameIn(new ArrayList<>(Arrays.asList("Wane", "Greg", "Jack")));


        Assertions.assertThat(userList).isNotNull();
        Assertions.assertThat(userList.size()).isEqualTo(3);;

    }
}
