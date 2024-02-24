package org.demo.chatweb.controllers;

import jakarta.servlet.ServletException;
import org.demo.chatweb.config.JWTUtil;
import org.demo.chatweb.models.User;
import org.demo.chatweb.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/status")
public class StatusController {
    private final JWTUtil jwtUtil;
    private final UsersService usersService;

    @Autowired
    public StatusController(JWTUtil jwtUtil, UsersService usersService) {
        this.jwtUtil = jwtUtil;
        this.usersService = usersService;
    }

    @PostMapping("/online")
    public ResponseEntity setStatusOnline(@RequestHeader(value = "Authorization") String authHeader) throws ServletException {
        String username = jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader);
        User user = usersService.getByUsername(username);
        usersService.saveAndSetStatus(user, true);
        return new ResponseEntity(HttpStatus.OK);
    }
    @PostMapping("/offline")
    public ResponseEntity setStatusOffline(@RequestHeader(value = "Authorization") String authHeader) throws ServletException {
        String username = jwtUtil.filterAndRetrieveClaimSpecificToken(authHeader);
        User user = usersService.getByUsername(username);
        usersService.saveAndSetStatus(user, false);
        return new ResponseEntity(HttpStatus.OK);
    }
}
