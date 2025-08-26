package com.booleanuk.api.controller;

import com.booleanuk.api.model.Author;
import com.booleanuk.api.repository.AuthorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("authors")
public class AuthorController {
    private final AuthorRepository repository;

    public AuthorController(AuthorRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Author>> getAll() {
        return ResponseEntity.ok(this.repository.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Author> getById(@PathVariable("id") Integer id) {
        Author author = this.repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find author with that id."));
        return ResponseEntity.ok(author);
    }

    record PostAuthor(String first_name, String last_name, String email, boolean alive) {}

    //@ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ResponseEntity<Author> create(@RequestBody PostAuthor request) {
        Author author = new Author(request.first_name(), request.last_name(), request.email(), request.alive());
        return new ResponseEntity<>(this.repository.save(author), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable int id, @RequestBody PostAuthor author) {
        Author authorToUpdate = this.repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find author with that id."));
        authorToUpdate.setFirstName(author.first_name());
        authorToUpdate.setLastName(author.last_name());
        authorToUpdate.setEmail(author.email());
        authorToUpdate.setAlive(author.alive());
        return new ResponseEntity<>(this.repository.save(authorToUpdate), HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Author> deleteAuthor(@PathVariable int id) {
        Author authorToDelete = this.repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find author with that id."));
        this.repository.delete(authorToDelete);
        return ResponseEntity.ok(authorToDelete);
    }
}
