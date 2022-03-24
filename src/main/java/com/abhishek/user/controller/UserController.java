package com.abhishek.user.controller;

import com.abhishek.user.entity.User;
import com.abhishek.user.entity.dto.UserResponse;
import com.abhishek.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private static final String TOPIC = "NewTopic";

    @Autowired
    private UserService userService;

    @Autowired
    KafkaTemplate<String, User> kafkaTemplate;

    @PostMapping("/")
    public User saveUser(@RequestBody User user) {
        log.info("Saving User with ID: "+user.getUserId());
        kafkaTemplate.send(TOPIC, user);
        return userService.saveUser(user);
    }

    @GetMapping("/{id}")
    public UserResponse getUserWithDepartment(@PathVariable("id") Long userId) {
        return userService.getUserWithDepartment(userId);
    }

}
