package com.booleanuk.api.controller;

import com.booleanuk.api.model.User;
import com.booleanuk.api.model.dto.UserDTO;
import com.booleanuk.api.repository.UserRepository;
import com.booleanuk.api.responses.ErrorResponse;
import com.booleanuk.api.responses.UserListResponse;
import com.booleanuk.api.responses.UserResponse;
import com.booleanuk.api.responses.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserRepository users;

    @GetMapping
    public ResponseEntity<UserListResponse> getAll() {
        UserListResponse UserListResponse = new UserListResponse();
        UserListResponse.set(this.users.findAll());
        return new ResponseEntity<>(UserListResponse, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Response<?>> getOne(@PathVariable int id) {
        User toReturn = this.users.findById(id).orElse(null);
        if (toReturn == null) {
            ErrorResponse errorResponse = new ErrorResponse("not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        UserResponse UserResponse = new UserResponse();
        UserResponse.set(toReturn);
        return new ResponseEntity<>(UserResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Response<?>> create(@RequestBody UserDTO toAdd) {
        // TODO: check if given username/email exists

        User actualToAdd = new User();
        actualToAdd.setCreatedOn(LocalDateTime.now());
        actualToAdd.setEmail(toAdd.getEmail());
        actualToAdd.setUsername(toAdd.getUsername());

        UserResponse UserResponse = new UserResponse();
        UserResponse.set(this.users.save(actualToAdd));
        return new ResponseEntity<>(UserResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Response<?>> delete(@PathVariable int id) {
        User toDelete = this.users.findById(id).orElse(null);
        if (toDelete == null) {
            ErrorResponse errorResponse = new ErrorResponse("not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        this.users.delete(toDelete);

        UserResponse UserResponse = new UserResponse();
        UserResponse.set(toDelete);
        return new ResponseEntity<>(UserResponse, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Response<?>> update(@PathVariable int id, @RequestBody UserDTO newData) {
        User toUpdate = this.users.findById(id).orElse(null);
        if (toUpdate == null) {
            ErrorResponse errorResponse = new ErrorResponse("not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        toUpdate.setUsername(newData.getUsername());
        toUpdate.setEmail(newData.getEmail());

        UserResponse UserResponse = new UserResponse();
        UserResponse.set(this.users.save(toUpdate));
        return new ResponseEntity<>(UserResponse, HttpStatus.OK);
    }
}
