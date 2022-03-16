package com.abhishek.user.controller;

import com.abhishek.user.entity.User;
import com.abhishek.user.entity.dto.UserResponse;
import com.abhishek.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/")
    public User saveUser(@RequestBody User user) {
        log.info("Saving User with ID: "+user.getUserId());
        return userService.saveUser(user);
    }

    @GetMapping("/{id}")
    public UserResponse getUserWithDepartment(@PathVariable("id") Long userId) {
        return userService.getUserWithDepartment(userId);
    }

}
