package com.abhishek.user.service;

import com.abhishek.user.entity.User;
import com.abhishek.user.entity.dto.Department;
import com.abhishek.user.entity.dto.UserResponse;
import com.abhishek.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Qualifier(value = "restTemplate")
    private RestTemplate restTemplate;

    public User saveUser(User user) {
        log.info("saving user in db");
        return userRepository.save(user);
    }


    public UserResponse getUserWithDepartment(Long userId) {
        UserResponse ur = new UserResponse();
        User user = userRepository.findUserByUserId(userId);
        log.info("going to call department service for user", user);
        Department dept = restTemplate.getForObject("http://localhost:9001/departments/"+user.getDepartmentId(), Department.class);
        ur.setDepartment(dept);
        ur.setUser(user);
        return ur;
    }
}
