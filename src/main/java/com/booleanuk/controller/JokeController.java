package com.booleanuk.controller;

import com.booleanuk.model.Joke;
import com.booleanuk.repository.JokeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("jokes")
public class JokeController {
    @Autowired
    private JokeRepository jokeRepository;

    private Base64 Base64;

    public JokeController(JokeRepository jokeRepository) {
        this.jokeRepository = jokeRepository;
    }

    record PostBook(String jokeText) {};

    @GetMapping
    public List<Joke> getBooks() {
        return jokeRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Joke> create(@RequestBody PostBook request){
        Joke joke = new Joke(Base64.getEncoder().encodeToString((request.jokeText).getBytes()));
        return new ResponseEntity<>(jokeRepository.save(joke), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Joke> getById(@PathVariable("id") Integer id){
        Joke joke = jokeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Joke not found")
        );

        byte[] actualByte= Base64.getDecoder().decode(joke.getJokeText().getBytes());
        joke.setJokeText(new String(actualByte));

        return ResponseEntity.ok(joke);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Joke> update(@PathVariable int id, @RequestBody PostBook request){
        Joke joke = this.jokeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Joke not found")
        );

        joke.setJokeText(request.jokeText);

        return new ResponseEntity<>(jokeRepository.save(joke), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Joke> delete(@PathVariable int id){
        Joke joke = this.jokeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Joke not found")
        );
        this.jokeRepository.delete(joke);
        return new ResponseEntity<>(joke, HttpStatus.OK);
    }
}