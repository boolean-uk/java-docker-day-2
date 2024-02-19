package com.booleanuk.api.controller;

import com.booleanuk.api.model.DTO.PostSingleDTO;
import com.booleanuk.api.model.DTO.UserAllDTO;
import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/posts")
@Tag(name = "Posts", description = "Posts API")
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;
    @GetMapping
    @Operation(summary = "Get all posts")
    public ResponseEntity<List<PostSingleDTO>> getAll() {
        List<Post> posts = this.postRepository.findAll();
        List<PostSingleDTO> postSingleDTOS = new ArrayList<>();
        for (Post p : posts) {
            postSingleDTOS.add(modelMapper.map(p, PostSingleDTO.class));
        }
        return new ResponseEntity<>(postSingleDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get post by id")
    public ResponseEntity<PostSingleDTO> getById(@PathVariable int id) {
        return this.postRepository.findById(id)
                .map(post -> new ResponseEntity<>(modelMapper.map(post, PostSingleDTO.class), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @Operation(summary = "Create post")
    public ResponseEntity<PostSingleDTO> create(@RequestBody Post post, @RequestParam int userId) {
        User author = this.userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        post.setAuthor(author);
        return new ResponseEntity<>(modelMapper.map(this.postRepository.save(post), PostSingleDTO.class), HttpStatus.CREATED);
    }
    @PostMapping("/{id}/like/{userId}")
    @Operation(summary = "Like post")
    public ResponseEntity<PostSingleDTO> like(@PathVariable int id, @PathVariable int userId) {
        Post post = this.postRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        post.getLikes().add(user);
        return new ResponseEntity<>(modelMapper.map(this.postRepository.save(post), PostSingleDTO.class), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostSingleDTO> update(@PathVariable int id, @RequestBody Post post) {
        Post postToUpdate = this.postRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        postToUpdate.setTitle(post.getTitle());
        postToUpdate.setContent(post.getContent());
        return new ResponseEntity<>(modelMapper.map(this.postRepository.save(postToUpdate), PostSingleDTO.class), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete post")
    public ResponseEntity<PostSingleDTO> delete(@PathVariable int id) {
        Post postToDelete = this.postRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
        this.postRepository.deleteById(id);
        return new ResponseEntity<>(modelMapper.map(postToDelete, PostSingleDTO.class), HttpStatus.OK);
    }

}
