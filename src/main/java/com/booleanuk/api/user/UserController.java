package com.booleanuk.api.post;

import com.booleanuk.api.response.*;
import com.booleanuk.api.response.Error;
import com.booleanuk.api.user.User;
import com.booleanuk.api.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    LocalDateTime currentTime = LocalDateTime.now();

    @GetMapping
    public ResponseEntity<Response> getAll() {
        return new ResponseEntity<>(new UserListResponse(this.userRepository.findAll()), HttpStatus.OK);
    }

    @GetMapping("{userId}")
    public ResponseEntity<Response> getUser(@PathVariable int userId) {
        User user = this.userRepository
                .findById(userId)
                .orElse(null);
        if (user == null) {
            return new ResponseEntity<>(new ErrorResponse(new Error("User not found")), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(new UserResponse(user), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Response> createUser(@RequestBody User user) {


        if(user.getUsername().isEmpty() || user.getEmail().isEmpty()){
            return new ResponseEntity<>(new ErrorResponse(new Error("Bad Request")), HttpStatus.BAD_REQUEST);
        }

        user.setCreatedAt(currentTime);
        user.setUpdatedAt(currentTime);
        this.userRepository.save(user);
        return new ResponseEntity<>(new UserResponse(user), HttpStatus.CREATED);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Response> deleteUser (@PathVariable int userId) {

        User user = this.userRepository
                .findById(userId)
                .orElse(null);
        if (user == null) {
            return new ResponseEntity<>(new ErrorResponse(new Error("User not found")), HttpStatus.NOT_FOUND);
        }


        this.userRepository.delete(user);
        return new ResponseEntity<>(new UserResponse(user), HttpStatus.OK);
    }

    @PutMapping("{userId}")
    public ResponseEntity<Response> updateUser (@PathVariable int userId, @RequestBody User user) {

        User userToUpdate = this.userRepository
                .findById(userId)
                .orElse(null);
        if (user == null) {
            return new ResponseEntity<>(new ErrorResponse(new Error("User not found")), HttpStatus.NOT_FOUND);
        }

        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setEmail(user.getEmail());


        userToUpdate.setUpdatedAt(currentTime);
        this.userRepository.save(userToUpdate);
        return new ResponseEntity<>(new UserResponse(userToUpdate), HttpStatus.OK);
    }
}
