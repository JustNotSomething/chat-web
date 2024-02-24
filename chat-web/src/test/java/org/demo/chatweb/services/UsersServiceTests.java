package org.demo.chatweb.services;

import org.demo.chatweb.models.User;
import org.demo.chatweb.repository.UsersRepository;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
@DataJpaTest
@ExtendWith(MockitoExtension.class)
public class UsersServiceTests {


    @Mock
    private UsersRepository usersRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsersService usersService;

    @Test
    public void UsersService_GetAll_ReturnAllUsers() {

        List<User> userList = new ArrayList<>();
        Mockito.when(usersRepository.findAll()).thenReturn(userList);


        List<User> result = usersService.index();


        assertEquals(userList, result);
    }

    @Test
    public void UsersService_SaveUser_ReturnUser() {

        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");

        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");
        Mockito.when(usersRepository.save(Mockito.any(User.class))).thenReturn(user);


        usersService.save(user);


        assertEquals("encodedPassword", user.getPassword());
        assertEquals("ROLE_USER", user.getRole());
        assertFalse(user.getHideProfile());
        assertFalse(user.getOnline());
    }

    @Test
    public void UsersService_SaveAndHideUser_ReturnUserWithHiddenProfile() {

        User user = new User();
        user.setUsername("testUser");


        usersService.saveAndHide(user, true);


        assertTrue(user.getHideProfile());
    }

    @Test
    public void UsersService_SaveAndSetAvatar_ReturnUserWithSetAvatar() {

        User user = new User();
        user.setUsername("testUser");
        String avatar = "avatar.jpg";


        usersService.saveAndSetAvatar(user, avatar);


        assertEquals(avatar, user.getAvatar());
    }

    @Test
    public void UsersService_SaveAndSetStatus_ReturnUserWithSetStatus() {

        User user = new User();
        user.setUsername("testUser");


        usersService.saveAndSetStatus(user, true);


        assertTrue(user.getOnline());
    }

    @Test
    public void UsersService_GetRoleByUsername_ReturnUserRole() {

        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        user.setRole("ROLE_ADMIN");

        Mockito.when(usersRepository.findByUsername(username)).thenReturn(Optional.of(user));


        String result = usersService.getRoleByUsername(username);


        assertEquals("ROLE_ADMIN", result);
    }

    @Test
    public void UsersService_GetEmailByUsername_ReturnUserEmail() {

        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        user.setEmail("test@example.com");

        Mockito.when(usersRepository.findByUsername(username)).thenReturn(Optional.of(user));


        String result = usersService.getEmailByUsername(username);


        assertEquals("test@example.com", result);
    }

    @Test
    public void UsersService_GetByUsername_ReturnUserByUsername() {

        String username = "testUser";
        User user = new User();
        user.setUsername(username);

        Mockito.when(usersRepository.findByUsername(username)).thenReturn(Optional.of(user));


        User result = usersService.getByUsername(username);


        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    @Test
    public void UsersService_FindUserById_ReturnUserById() {

        int userId = 1;
        User user = new User();
        user.setId(userId);

        Mockito.when(usersRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = usersService.find(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

    @Test
    public void UsersService_DeleteUser_DeletesUser() {

        User user = new User();

        usersService.delete(user);

        Mockito.verify(usersRepository, times(1)).delete(user);
    }

    @Test
    public void UsersService_UpdateUser_UpdatesUserRole() {

        User userToUpdate = new User();
        userToUpdate.setRole("ROLE_ADMIN");

        usersService.update(userToUpdate);

        Mockito.verify(usersRepository, times(1)).save(Mockito.any(User.class));
    }

    @Test
    public void UsersService_GetByUsernames_ReturnUsersByUsernames() {

        List<String> usernames = List.of("user1", "user2");
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());

        Mockito.when(usersRepository.findByUsernameIn(usernames)).thenReturn(users);


        List<User> result = usersService.getByUsernames(usernames);

        assertEquals(users, result);
    }

    @Test
    public void UsersService_GiveAdminRights_ReturnUserWithAdminRole() {

        User user = new User();

        usersService.giveAdminRights(user);

        assertEquals("ROLE_ADMIN", user.getRole());
    }
}
