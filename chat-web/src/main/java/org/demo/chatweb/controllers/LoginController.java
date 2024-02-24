package org.demo.chatweb.controllers;

import org.demo.chatweb.config.JWTUtil;
import org.demo.chatweb.dto.AuthenticationDTO;
import org.demo.chatweb.models.User;
import org.demo.chatweb.services.UsersService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/login")
public class LoginController {


    private final UsersService usersService;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);;
    @Autowired
    public LoginController(UsersService usersService, JWTUtil jwtUtil, ModelMapper modelMapper, AuthenticationManager authenticationManager) {
        this.usersService = usersService;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
    public Map<String, String> login(@RequestBody AuthenticationDTO authenticationDTO)
    {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(), authenticationDTO.getPassword());

        try{
            authenticationManager.authenticate(authenticationToken);
        }
        catch (BadCredentialsException e)
        {
            logger.warn("Authorisation error | User: " + authenticationDTO.getUsername() + " | " + e.getMessage());
            return  Map.of("message", "error");
        }

        User user = usersService.getByUsername(authenticationDTO.getUsername());

        String userRole = user.getRole();
        String userEmail = user.getEmail();


        String token = jwtUtil.generateToken(authenticationDTO.getUsername(), userRole, userEmail);
        System.out.println(token);

        logger.info("User: " + authenticationDTO.getUsername() + " | Successfully logged in");
        logger.info(authenticationDTO.getUsername() + " jwt-token: " + token);
        return  Map.of("jwt-token", token);
    }


}
