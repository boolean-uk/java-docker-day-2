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
  public ResponseEntity<List<User>> get() {
    return ResponseEntity.ok(this.repository.findAll());
  }

  @GetMapping("{id}")
  public ResponseEntity<User> get(@PathVariable int id) {
    return ResponseEntity.ok(this.repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
  }

  @PostMapping
  public ResponseEntity<User> post(@RequestBody User user) {
    return ResponseEntity.status(HttpStatus.CREATED).body(this.repository.save(user));
  }

  @PutMapping("{id}")
  public ResponseEntity<User> put(@PathVariable int id, @RequestBody User user) {
    return ResponseEntity.status(HttpStatus.CREATED).body(this.repository.findById(id)
        .map(existing -> {
          existing.update(user);
          return this.repository.save(existing);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
  }

  @DeleteMapping
  public ResponseEntity<User> delete(@PathVariable int id) {
    return ResponseEntity.ok(this.repository.findById(id)
        .map(existing -> {
          this.repository.deleteById(id);
          return existing;
        })
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
  }
}