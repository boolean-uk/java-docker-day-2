package com.booleanuk.api.controllers;

import com.booleanuk.api.models.User;
import com.booleanuk.api.payload.response.Response;
import com.booleanuk.api.payload.response.SuccessResponse;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Response<List<User>>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(this.userRepository.findAll()));
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Response<?>> deleteUser(@PathVariable int id) {
//        User userToDelete = findUser(id);
//        if(userToDelete == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
//        }
//        if(!userToDelete.getLoans().isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("bad request"));
//        }
//
//        userRepository.delete(userToDelete);
//        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>(userToDelete));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Response<?>> updateUser(@PathVariable int id, @RequestBody User user) {
//        User userToUpdate = findUser(id);
//        if(userToUpdate == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("not found"));
//        }
//
//        if(user.getUsername() != null) {
//            userToUpdate.setUsername(user.getUsername());
//        }
//        if(user.getPassword() != null) {
//            userToUpdate.setPassword(user.getPassword());
//        }
//        if(user.getEmail() != null) {
//            userToUpdate.setEmail(user.getEmail());
//        }
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>(userRepository.save(userToUpdate)));
//    }

    private User findUser(int id) {
        return userRepository.findById(id).orElse(null);
    }

    private boolean containsNull(User user) {
        return user.getUsername() == null || user.getEmail() == null || user.getPassword() == null;
    }
}


