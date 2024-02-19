package com.booleanuk.api.controllers;

import com.booleanuk.api.models.User;
import com.booleanuk.api.repositories.UserRepository;
import com.booleanuk.api.responses.ErrorResponse;
import com.booleanuk.api.responses.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    private LocalDateTime today;

    @GetMapping
    public ResponseEntity<Response<?>> getAll(){
        Response<List<User>> response = new Response<>();
        response.set(this.userRepository.findAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getOne(@PathVariable int id){
        User user = findTheUser(id);
        if (user == null){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("User not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        Response<User> response = new Response<>();
        response.set(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Response<?>> create(@RequestBody User user){
        today = LocalDateTime.now();

        if (this.userRepository.findByUsername(user.getUsername()).isPresent()){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Username is taken");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        if (user.getUsername() == null ||
        user.getName() == null ||
        user.getEmail() == null){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Bad request");
            new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
        }
        Response<User> response = new Response<>();
        user.setCreatedAt(String.valueOf(today));
        response.set(this.userRepository.save(user));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> update(@PathVariable int id, @RequestBody User user){
        today = LocalDateTime.now();
        User updateUser = findTheUser(id);
        if (updateUser == null){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("User not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        if (user.getUsername() == null ||
                user.getName() == null ||
                user.getEmail() == null){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Bad request");
            new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
        }
        Optional.ofNullable(user.getName())
                .ifPresent(updateUser::setName);
        Optional.ofNullable(user.getUsername())
                .ifPresent(updateUser::setUsername);
        Optional.ofNullable(user.getEmail())
                .ifPresent(updateUser::setEmail);
        Response<User> response = new Response<>();
        response.set(this.userRepository.save(updateUser));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> delete(@PathVariable int id){
        User user = findTheUser(id);
        if (user == null){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("User not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        Response<User> response = new Response<>();
        this.userRepository.delete(user);
        response.set(user);
        user.setPosts(new ArrayList<>());
        return ResponseEntity.ok(response);
    }


    private User findTheUser(int id){
        return this.userRepository.findById(id)
                .orElse(null);
    }
}
