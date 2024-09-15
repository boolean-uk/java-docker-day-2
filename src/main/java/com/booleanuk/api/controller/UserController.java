package com.booleanuk.api.controller;

import com.booleanuk.api.model.Post;
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
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @GetMapping
    public ResponseEntity<UserListResponse> getAllUser(){
        UserListResponse response = new UserListResponse();
        response.set(this.userRepository.findAll());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createUser(@RequestBody User user){
        User user1;
        try {
            user1 = this.userRepository.save(user);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Could not create a new user, please check all fields are correct.");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        UserResponse userResponse = new UserResponse();
        userResponse.set(user1);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateUser(@PathVariable int id, @RequestBody User user){
        User user1 = this.getAUser(id);
        if (user1 == null){
            ErrorResponse error = new ErrorResponse();
            error.set("No user with that id found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } try {
            user1.setUserName(user.getUserName());
            user1.setEmail(user.getEmail());
            user1.setPhone(user.getPhone());
            this.userRepository.save(user1);
        } catch (Exception e) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Could not update user, please check all fields are correct.");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        UserResponse userResponse = new UserResponse();
        userResponse.set(user1);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteUser(@PathVariable int id){
        User user1 = this.getAUser(id);
        if (user1 == null){
            ErrorResponse error = new ErrorResponse();
            error.set("No user with that id found.");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        if (!user1.getPost().isEmpty()){
            for (Post post : user1.getPost()){
                this.postRepository.delete(post);
            }
        }
        this.userRepository.delete(user1);
        UserResponse userResponse = new UserResponse();
        userResponse.set(user1);
        return ResponseEntity.ok(userResponse);
    }

    private User getAUser(int id){
        return this.userRepository.findById(id).orElse(null);
    }
}
