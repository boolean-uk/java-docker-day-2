package com.booleanuk.api.controller;

import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.User;
import com.booleanuk.api.payload.response.ErrorResponse;
import com.booleanuk.api.payload.response.Response;
import com.booleanuk.api.payload.response.UserListResponse;
import com.booleanuk.api.payload.response.UserResponse;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Response<?>> getAllUsers()    {
        UserListResponse userListResponse = new UserListResponse();
        userListResponse.set(this.userRepository.findAll());
        return ResponseEntity.ok(userListResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<Response<?>> getOneUser(@PathVariable int id) {
        User user = this.userRepository.findById(id).orElse(null);
        if(user == null)
        {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("No user by that id was found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        UserResponse userResponse = new UserResponse();
        userResponse.set(user);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createUser(@RequestBody User user)   {
        if(user.getUsername() == null
        || user.getEmail() == null) {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("One or more required fields are null");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        if(user.getUsername().contains(" "))
        {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Username cannot contain whitespace");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        this.userRepository.save(user);
        UserResponse userResponse = new UserResponse();
        userResponse.set(user);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Response<?>> updateUser(@RequestBody User user, @PathVariable int id) {
        User userToUpdate = this.userRepository.findById(id).orElse(null);
        if(userToUpdate == null)    {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("No user by that id was found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        if(user.getEmail() != null)
            userToUpdate.setEmail(user.getEmail());

        this.userRepository.save(userToUpdate);
        UserResponse userResponse = new UserResponse();
        userResponse.set(userToUpdate);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Response<?>> deleteUser(@PathVariable int id) {
        User userToDelete = this.userRepository.findById(id).orElse(null);
        if(userToDelete == null)    {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("No user by that id was found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        for(Post p : userToDelete.getPosts())
        {
            userToDelete.removePost(p);
        }

        this.userRepository.delete(userToDelete);
        UserResponse userResponse = new UserResponse();
        userResponse.set(userToDelete);
        return ResponseEntity.ok(userResponse);
    }
}
