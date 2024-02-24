package org.demo.chatweb.services;

import org.demo.chatweb.models.User;
import org.demo.chatweb.repository.UsersRepository;
import org.demo.chatweb.security.UsersDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailService implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Autowired
    public UserDetailService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = usersRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new UsersDetails(user.get());
    }
}