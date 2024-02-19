package com.booleanuk.api.controllers;

import com.booleanuk.api.HelperUtils;
import com.booleanuk.api.models.Post;
import com.booleanuk.api.models.User;
import com.booleanuk.api.repositories.PostRepository;
import com.booleanuk.api.repositories.UserRepository;
import com.booleanuk.api.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllUsers() {
        List<User> userList = this.userRepository.findAll();
        return HelperUtils.okRequest(userList);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createUser(@RequestBody User user) {
        return HelperUtils.createdRequest(this.userRepository.save(user));
    }
}
