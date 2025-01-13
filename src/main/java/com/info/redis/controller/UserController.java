package com.info.redis.controller;

import com.info.redis.entity.User;
import com.info.redis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redis-users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public User saveUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "User deleted";
    }
}

