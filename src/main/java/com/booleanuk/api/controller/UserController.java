package com.booleanuk.api.controller;

import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.UserRepository;
import com.booleanuk.api.response.ErrorResponse;
import com.booleanuk.api.response.Response;
import com.booleanuk.api.response.UserListResponse;
import com.booleanuk.api.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    private ErrorResponse errorResponse = new ErrorResponse();
    private UserResponse userResponse = new UserResponse();
    private UserListResponse userListResponse = new UserListResponse();

    @GetMapping
    public ResponseEntity<Response<?>> getAllUsers() {
        this.userListResponse.set(this.userRepository.findAll());
        return ResponseEntity.ok(userListResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createUser (@RequestBody User user) {
        try {
            this.userResponse.set(this.userRepository.save(user));
        } catch (Exception e) {
            this.errorResponse.set("Bad request");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Response<?>> getOneUser(@PathVariable int id) {
        User user = this.userRepository.findById(id).orElse(null);
        if (user == null) {
            this.errorResponse.set("Not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        this.userResponse.set(user);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("{id}")
    public ResponseEntity<Response<?>> updateUser(@PathVariable int id, @RequestBody User user) {
        User userToUpdate = this.userRepository.findById(id).orElse(null);
        if (userToUpdate == null) {
            this.errorResponse.set("Not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setEmail(user.getEmail());
        try {
            userToUpdate = this.userRepository.save(userToUpdate);
        } catch (Exception e) {
            this.errorResponse.set("Bad request");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        this.userResponse.set(userToUpdate);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Response<?>> deleteUser(@PathVariable int id) {
        User userToDelete = this.userRepository.findById(id).orElse(null);
        if (userToDelete == null) {
            errorResponse.set("Not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        this.userRepository.delete(userToDelete);
        userResponse.set(userToDelete);
        return ResponseEntity.ok(userResponse);
    }
}
