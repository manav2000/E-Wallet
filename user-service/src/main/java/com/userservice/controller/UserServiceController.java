package com.userservice.controller;

import javax.validation.Valid;

import com.userservice.entity.User;
import com.userservice.request.UserCreateRequest;
import com.userservice.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserServiceController {

    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public Integer createUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {

        return userService.save(userCreateRequest.toUser());
    }

    @GetMapping("/user")
    public User getUser(@RequestParam("id") int id){
        
        return userService.findById(id);
    }
}
