package com.booleanuk.api.controller;


import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.PostRepository;
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
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @GetMapping
    public ResponseEntity<UserListResponse> getAllUsers() {
        UserListResponse userListResponse = new UserListResponse();
        userListResponse.set(this.userRepository.findAll());
        return ResponseEntity.ok(userListResponse);
    }


    @PostMapping
    public ResponseEntity<Response<?>> create(@RequestBody User user) {
        User userCreate;

        try {
            userCreate = this.userRepository.save(user);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Bad request");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        UserResponse userResponse = new UserResponse();
        userResponse.set(userCreate);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> update(@PathVariable int id, @RequestBody User user) {
        User user1 = this.userRepository.findById(id).orElse(null);
        if (user1 == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        try {

            user1.setName(user.getName());
            user1.setContactInfo(user.getContactInfo());
            user1.setPassword(user.getPassword());
            this.userRepository.save(user1);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Bad request");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        UserResponse userResponse = new UserResponse();
        userResponse.set(user1);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> delete(@PathVariable int id) {
        User userDelete = this.userRepository.findById(id).orElse(null);
        if (userDelete == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Not found student");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        this.userRepository.delete(userDelete);
        UserResponse userResponse = new UserResponse();
        userResponse.set(userDelete);
        return ResponseEntity.ok(userResponse);
    }
}
