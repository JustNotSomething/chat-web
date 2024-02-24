package org.demo.chatweb.controllers;

import org.demo.chatweb.models.User;
import org.demo.chatweb.services.UsersService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/createMany")
public class TestController {
    private final UsersService usersService;

    public TestController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping
    public void createMany()
    {
        for (int i = 0; i < 100; i++)
        {
            User user = new User();
            user.setUsername("Test" + i);
            user.setEmail("test" +i+"@gmail.com");
            user.setDateOfBirth(new Date());
            user.setPhone("+11111111111");
            user.setPassword("1111");
            usersService.save(user);
        }
    }
}
