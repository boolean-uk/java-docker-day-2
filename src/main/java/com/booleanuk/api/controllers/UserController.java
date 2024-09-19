package com.booleanuk.api.controllers;

import com.booleanuk.api.models.User;
import com.booleanuk.api.repositories.BlogPostRepository;
import com.booleanuk.api.repositories.CommentRepository;
import com.booleanuk.api.repositories.UserRepository;
import com.booleanuk.api.responses.ErrorResponse;
import com.booleanuk.api.responses.Response;
import com.booleanuk.api.responses.UserListResponse;
import com.booleanuk.api.responses.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private CommentRepository commentRepository;

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody User user) {
        this.userRepository.save(user);
        UserResponse response = new UserResponse();
        response.set(user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<UserListResponse> getAll() {
        UserListResponse response = new UserListResponse();
        response.set(this.userRepository.findAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<Response<?>> getSpecific(@PathVariable int id) {
        User user = this.userRepository.findById(id).orElse(null);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("A user with this id was not found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        UserResponse response = new UserResponse();
        response.set(user);
        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<Response<?>> update(@PathVariable int id, @RequestBody User user) {
        User originalUser = this.userRepository.findById(id).orElse(null);
        if (originalUser == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        user.setId(id);
        try {
            this.userRepository.save(user);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        UserResponse response = new UserResponse();
        response.set(user);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Response<?>> delete(@PathVariable int id) {
        User user = this.userRepository.findById(id).orElse(null);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("Not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        this.userRepository.delete(user);
        UserResponse response = new UserResponse();
        response.set(user);
        return ResponseEntity.ok(response);
    }
}
