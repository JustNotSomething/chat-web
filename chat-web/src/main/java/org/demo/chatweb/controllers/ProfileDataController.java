package org.demo.chatweb.controllers;

import jakarta.servlet.ServletException;
import org.demo.chatweb.config.JWTUtil;
import org.demo.chatweb.converters.UserConverter;
import org.demo.chatweb.converters.UserDTOConverter;
import org.demo.chatweb.dto.UserDTO;
import org.demo.chatweb.services.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileDataController {
    private final JWTUtil jwtUtil;
    private final UsersService usersService;
    private final UserDTOConverter userDTOConverter;
    private final UserConverter userConverter;
    private static final Logger logger = LoggerFactory.getLogger(ProfileDataController.class);;


    @Autowired
    public ProfileDataController(JWTUtil jwtUtil, UsersService usersService, UserDTOConverter userDTOConverter, UserConverter userConverter) {
        this.jwtUtil = jwtUtil;
        this.usersService = usersService;
        this.userDTOConverter = userDTOConverter;
        this.userConverter = userConverter;
    }

    @GetMapping
    public ResponseEntity<UserDTO> userProfileData(@RequestHeader(value = "Authorization") String authHeader) throws ServletException {

            String username = jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader);
            UserDTO userDTO = userDTOConverter.convertToUserDTO(usersService.getByUsername(username));

            logger.info("Load user - " + username + " profile data");

            return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }





}
