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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteUser(@PathVariable int id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {

            this.userRepository.deleteById(id);

            userOptional.get().setPosts(new ArrayList<>());
            userOptional.get().setComments(new ArrayList<>());

            return HelperUtils.okRequest(userOptional);
        } else {
            return HelperUtils.badRequest(new ApiResponse.Message("NO"));
        }
    }
}
