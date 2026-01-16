package com.booleanuk.api.controller;

import com.booleanuk.api.model.Post;
import com.booleanuk.api.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("posts")
public class PostController {
  @Autowired
  private PostRepository repository;

  @GetMapping
  public ResponseEntity<List<Post>> get() {
    return ResponseEntity.ok(this.repository.findAll());
  }

  @GetMapping("{id}")
  public ResponseEntity<Post> get(@PathVariable int id) {
    return ResponseEntity.ok(this.repository.findById(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
  }

  @PostMapping
  public ResponseEntity<Post> post(@RequestBody Post post) {
    return ResponseEntity.status(HttpStatus.CREATED).body(this.repository.save(post));
  }

  @PutMapping("{id}")
  public ResponseEntity<Post> put(@PathVariable int id, @RequestBody Post post) {
    return ResponseEntity.status(HttpStatus.CREATED).body(this.repository.findById(id)
        .map(existing -> {
          existing.update(post);
          return this.repository.save(existing);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
  }

  @DeleteMapping
  public ResponseEntity<Post> delete(@PathVariable int id) {
    return ResponseEntity.ok(this.repository.findById(id)
        .map(existing -> {
          this.repository.deleteById(id);
          return existing;
        })
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
  }
}