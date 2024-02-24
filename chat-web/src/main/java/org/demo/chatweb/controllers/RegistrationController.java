package org.demo.chatweb.controllers;

import org.demo.chatweb.converters.UserConverter;
import org.demo.chatweb.dto.UserDTO;
import org.demo.chatweb.models.User;
import org.demo.chatweb.services.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registration")
public class RegistrationController {
    private final UsersService usersService;

    private final UserConverter userConverter;
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);;


    @Autowired
    public RegistrationController(UsersService usersService, UserConverter userConverter) {
        this.usersService = usersService;

        this.userConverter = userConverter;

    }

    @PostMapping
    public ResponseEntity save(@RequestBody UserDTO userDTO)
    {


        User user = userConverter.convertToUser((userDTO));
        System.out.println(user);
        usersService.save(user);
        logger.info("New user created | " + user.getUsername());
        return new ResponseEntity(HttpStatus.OK);
    }



}
