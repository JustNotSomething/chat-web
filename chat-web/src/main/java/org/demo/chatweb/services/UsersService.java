package org.demo.chatweb.services;

import org.demo.chatweb.models.User;
import org.demo.chatweb.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UsersService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> index()
    {
        return usersRepository.findAll();
    }

    @Transactional
    public void save(User user)
    {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        user.setRole("ROLE_USER");
        user.setHideProfile(false);
        user.setOnline(false);
        usersRepository.save(user);

    }

    @Transactional
    public void saveAndHide(User user, Boolean hide )
    {

        user.setHideProfile(hide);
        usersRepository.save(user);

    }

    @Transactional
    public void saveAndSetAvatar(User user, String avatar)
    {

        user.setAvatar(avatar);
        usersRepository.save(user);

    }
    @Transactional
    public void saveAndSetStatus(User user, Boolean status)
    {

        user.setOnline(status);
        usersRepository.save(user);

    }

    public String getRoleByUsername(String username)
    {
        Optional <User> user = usersRepository.findByUsername(username);

        return user.get().getRole();
    }

    public String getEmailByUsername(String username)
    {
        Optional <User> user = usersRepository.findByUsername(username);

        return user.get().getEmail();
    }

    public User getByUsername(String username)
    {
        Optional <User> user = usersRepository.findByUsername(username);
        return user.get();
    }

    public User find(int id)
    {
        return usersRepository.findById(id).orElse(null);
    }
    @Transactional
    public void delete(User user)
    {
        usersRepository.delete(user);
    }

    @Transactional
    public void update(User userToUpdate)
    {
        User user = new User();
        user.setRole(userToUpdate.getRole());
        usersRepository.save(user);
    }
    public List<User> getByUsernames(List<String> usernames) {
        return usersRepository.findByUsernameIn(usernames);
    }

    @Transactional
    public void giveAdminRights(User user)
    {
        user.setRole("ROLE_ADMIN");
        usersRepository.save(user);
    }

}
