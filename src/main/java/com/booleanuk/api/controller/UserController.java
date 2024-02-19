package com.booleanuk.api.controller;

import com.booleanuk.api.model.User;
import com.booleanuk.api.payload.response.ErrorResponse;
import com.booleanuk.api.payload.response.UserListResponse;
import com.booleanuk.api.payload.response.UserResponse;
import com.booleanuk.api.payload.response.Response;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<UserListResponse> getAllUsers() {


        UserListResponse listResponse = new UserListResponse();
        listResponse.set(this.userRepository.findAll());
        return ResponseEntity.ok(listResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getUserById(@PathVariable int id) {
        User user = this.userRepository.findById(id).orElse(null);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        UserResponse userResponse = new UserResponse();
        userResponse.set(user);
        return ResponseEntity.ok(userResponse);
    }

}
