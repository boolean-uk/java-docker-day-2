package com.booleanuk.api.controller;

import com.booleanuk.api.models.User;
import com.booleanuk.api.payload.response.ErrorResponse;
import com.booleanuk.api.payload.response.Response;
import com.booleanuk.api.payload.response.UserListResponse;
import com.booleanuk.api.payload.response.UserResponse;
import com.booleanuk.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{userId}/addFriend/{friendId}")
    public ResponseEntity<Response<?>> addFriend(@PathVariable int userId, @PathVariable int friendId) {
        User user = userRepository.findById(userId).orElse(null);
        User friend = userRepository.findById(friendId).orElse(null);
        if (user == null || friend == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("User or friend not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        user.getFriends().add(friend);
        userRepository.save(user);
        UserResponse userResponse = new UserResponse();
        userResponse.set(user);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/{userId}/friends")
    public ResponseEntity<Response<?>> getUserFriends(@PathVariable int userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("User not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        UserListResponse userListResponse = new UserListResponse();
        userListResponse.set(new ArrayList<>(user.getFriends()));
        return ResponseEntity.ok(userListResponse);
    }
}

