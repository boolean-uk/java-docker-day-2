package com.booleanuk.api.controller;

import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserRepository repository;


    @GetMapping
    public ResponseEntity<List<User>> getAll(){
        return ResponseEntity.ok(this.repository.findAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable int id){
        User employee = this.repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "This is not found by dave!"));
        return ResponseEntity.ok(employee);
    }

    @PostMapping
    public ResponseEntity<User> add(@RequestBody User employee){
        return new ResponseEntity<>(this.repository.save(employee), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update( @PathVariable int id,@RequestBody User employee){
        User empToUpdate = this.repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "This is not found by dave!"));
        empToUpdate.setEmail(employee.getEmail());
        empToUpdate.setFirstName(employee.getFirstName());
        empToUpdate.setIsActive(employee.getIsActive());

        return new ResponseEntity<>(this.repository.save(empToUpdate), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> delete(@PathVariable int id){
        User empToBeDeleted = this.repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "This is not found by dave!"));
        this.repository.delete(empToBeDeleted);
        return ResponseEntity.ok(empToBeDeleted);
    }

    
//    @GetMapping("{id}")
//    public User getById(@PathVariable("id") Integer id) {
//        return this.repository.findById(id).orElseThrow();
//    }
//
//    record PostUser(String email, String genre) {}
//
//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping
//    public User create(@RequestBody PostUser request) {
//        User user = new User(request.email(), request.genre());
//        return this.repository.save(user);
//    }
}
