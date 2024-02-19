package com.booleanuk.api.controllers;

import com.booleanuk.api.models.Interaction;
import com.booleanuk.api.models.Post;
import com.booleanuk.api.models.User;
import com.booleanuk.api.repositories.InteractionRepository;
import com.booleanuk.api.repositories.PostRepository;
import com.booleanuk.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class InteractionController {
    @Autowired
    private InteractionRepository repo;

    @Autowired
    private UserRepository users;

    @Autowired
    private PostRepository posts;

    @PostMapping
    public ResponseEntity<Interaction> create(@RequestBody Interaction interaction,
                                              @RequestParam(name = "user_id") int userId,
                                              @RequestParam(name = "post_id") int postId){
        User tempUser = users
                .findById(userId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));
        Post tempPost = posts
                .findById(postId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));

        interaction.setUser(tempUser);
        interaction.setPost(tempPost);
        interaction.setInteractedAt(nowFormatted());
        repo.save(interaction);

        return new ResponseEntity<>(interaction, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Interaction>> getAll(){
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping
    public ResponseEntity<List<Interaction>> getAllFromUser(@RequestParam(name = "user_id") int userId){
        User tempUser = users
                .findById(userId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(tempUser.getInteractions());
    }

    @GetMapping
    public ResponseEntity<List<Interaction>> getAllFromPost(@RequestParam(name = "post_id") int postId){
        Post tempPost = posts
                .findById(postId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(tempPost.getInteractions());
    }

    @GetMapping("{id}")
    public ResponseEntity<Interaction> getOne(@PathVariable int id){
        Interaction interaction = getById(id);

        return ResponseEntity.ok(interaction);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Interaction> delete(@PathVariable int id){
        Interaction toDelete = getById(id);
        repo.delete(toDelete);

        return ResponseEntity.ok(toDelete);
    }

    private Interaction getById(int id){
        return repo
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    private String nowFormatted(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return now.format(format);
    }
}
