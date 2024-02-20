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

    @GetMapping
    public ResponseEntity<UserListResponse> getAllUser() {
        UserListResponse userListResponse = new UserListResponse();
        userListResponse.set(this.userRepository.findAll());
        return ResponseEntity.ok(userListResponse);
    }

    private User getAUser(int id) {
        return this.userRepository.findById(id).orElse(null);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getUserById(@PathVariable int id) {
        User user = this.getAUser(id);
        if (user == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        UserResponse userResponse = new UserResponse();
        userResponse.set(user);
        return ResponseEntity.ok(userResponse);

    }

    @PostMapping
    public ResponseEntity<Response<?>> createABook(@RequestBody User user) {
        if(user.getName() == null || user.getEmail() == null){
            ErrorResponse error = new ErrorResponse();
            error.set("bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        User savedUser = this.userRepository.save(user);
        UserResponse userResponse = new UserResponse();
        userResponse.set(savedUser);

        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateUser(@PathVariable int id, @RequestBody User user){
        User userToUpdate = this.getAUser(id);
        if(userToUpdate == null){
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        if(user.getName() == null || user.getEmail() == null){
            ErrorResponse error = new ErrorResponse();
            error.set("bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        userToUpdate.setName(user.getName());
        userToUpdate.setEmail(user.getEmail());

        User updatedUser = this.userRepository.save(userToUpdate);

        UserResponse userResponse = new UserResponse();
        userResponse.set(updatedUser);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteUser (@PathVariable int id){
        User userToDelete = this.getAUser(id);
        if(userToDelete == null){
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        this.userRepository.delete(userToDelete);

        UserResponse userResponse = new UserResponse();
        userResponse.set(userToDelete);
        return ResponseEntity.ok(userResponse);
    }
}