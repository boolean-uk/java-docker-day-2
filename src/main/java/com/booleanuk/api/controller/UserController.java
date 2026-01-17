package com.booleanuk.api.controller;

import com.booleanuk.api.exceptions.CustomDataNotFoundException;
import com.booleanuk.api.exceptions.CustomParamaterConstraintException;
import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.UserRepository;
import com.booleanuk.api.response.Response;
import com.booleanuk.api.response.SuccessResponse;
import com.booleanuk.api.util.DateCreater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

/*    @PostMapping
    public ResponseEntity<Response<?>> createUser(@RequestBody User user) {

        User user1 = new User(user.getUsername(), user.getEmail(), user.getPassword());
        checkValidInput(user1);

        this.userRepository.save(user1);

        return new ResponseEntity<>(new SuccessResponse(user1), HttpStatus.CREATED);
    }*/


    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return new ResponseEntity<>(this.userRepository.findAll(), HttpStatus.OK);
    }



    @GetMapping("{id}")
    public ResponseEntity<Response<?>> getSpecificUser(@PathVariable(name = "id") int id) {
        User library = this.getAUser(id);

        return new ResponseEntity<>(new SuccessResponse(library), HttpStatus.OK);

    }

    @PutMapping("{id}")
    public ResponseEntity<Response<?>> updateUser(@PathVariable (name = "id") int id, @RequestBody User user) {
        User user1 = getAUser(id);
        user1.setUpdatedAt(DateCreater.getCurrentDate());
        user1.setUsername(user.getUsername());
        user1.setEmail(user.getEmail());
        user1.setPassword(user.getPassword());


        this.checkValidInput(user1);

        return new ResponseEntity<>(new SuccessResponse(user1), HttpStatus.CREATED);

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Response<?>> deleteUser(@PathVariable (name = "id") int id) {
        User user = this.getAUser(id);

        this.userRepository.delete(this.getAUser(id));

        return new ResponseEntity<>(new SuccessResponse(user), HttpStatus.OK);

    }

    private User getAUser(int id) {
        return this.userRepository.findById(id).orElseThrow(() -> new CustomDataNotFoundException("No user with that ID found"));
    }




    private void checkValidInput(User user) {
        if(user.getCreatedAt() == null || user.getUsername() == null || user.getUpdatedAt() == null || user.getEmail() == null || user.getPassword() == null) {
            throw new CustomParamaterConstraintException("Bad request");
        }
    }
}
