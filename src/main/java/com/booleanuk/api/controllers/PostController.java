package com.booleanuk.api.controllers;

import com.booleanuk.api.models.Post;
import com.booleanuk.api.models.User;
import com.booleanuk.api.repositories.PostRepository;
import com.booleanuk.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("posts")
public class PostController {
    @Autowired
    private PostRepository repo;

    @Autowired
    private UserRepository users;

    @PostMapping
    public ResponseEntity<Post> create(@RequestBody Post post, @RequestParam(name = "user_id") int userId){
        User tempUser = users
                .findById(userId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));

        post.setUser(tempUser);
        post.setInteractions(new ArrayList<>());
        post.setCreatedAt(nowFormatted());
        repo.save(post);

        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<Post> update(@PathVariable int id, @RequestBody Post post){
        Post toUpdate = getById(id);

        toUpdate.setContent(post.getContent());
        toUpdate.setUpdateAt(nowFormatted());
        repo.save(toUpdate);

        return new ResponseEntity<>(toUpdate, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAll(){
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping()
    public ResponseEntity<List<Post>> getAllFromUser(@RequestParam(name = "user_id") int userId){
        User tempUser = users
                .findById(userId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(tempUser.getPosts());
    }

    @GetMapping("{id}")
    public ResponseEntity<Post> getOne(@PathVariable int id){
        Post post = getById(id);

        return ResponseEntity.ok(post);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Post> delete(@PathVariable int id){
        Post toDelete = getById(id);

        toDelete.setInteractions(new ArrayList<>());
        repo.delete(toDelete);

        return ResponseEntity.ok(toDelete);
    }

    private Post getById(int id){
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
