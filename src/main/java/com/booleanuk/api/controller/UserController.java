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

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @GetMapping
    public ResponseEntity<Response<?>> getAllUsers(){
        List<User> students = this.userRepository.findAll();
        UserListResponse response = new UserListResponse();

        response.set(students);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createUser(@RequestBody User user){

        if (user.getUsername() == null){
            ErrorResponse error = new ErrorResponse();
            error.set("Could not create a new user, please check your username's format");

            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }


        UserResponse response = new UserResponse();

        this.userRepository.save(user);
        response.set(user);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Response<?>> updateUser(@PathVariable int id, @RequestBody User user){
        if (user.getUsername() == null){
            ErrorResponse error = new ErrorResponse();
            error.set("Could not update student, please check your username");

            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        User userToUpdate = this.userRepository.findById(id).orElse(null);

        if (userToUpdate == null){
            ErrorResponse error = new ErrorResponse();
            error.set("No user with that id found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setFollowers(user.getFollowers());
        userToUpdate.setFollowing(user.getFollowing());
        userToUpdate.setBio(user.getBio());

        UserResponse response = new UserResponse();

        this.userRepository.save(userToUpdate);
        response.set(userToUpdate);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Response<?>> deleteUser(@PathVariable int id){

        User userToDelete = this.userRepository.findById(id).orElse(null);
        if (userToDelete == null){
            ErrorResponse error = new ErrorResponse();
            error.set("No user with that id found");

            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        UserResponse response = new UserResponse();

        this.userRepository.delete(userToDelete);
        response.set(userToDelete);

        return ResponseEntity.ok(response);
    }
}
