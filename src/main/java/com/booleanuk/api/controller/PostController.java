package com.booleanuk.api.controller;

import com.booleanuk.api.dto.PostDTO;
import com.booleanuk.api.dto.PostMapper;
import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("posts")
public class PostController {

    PostRepository postRepository;

    UserRepository userRepository;

    PostMapper postMapper;

    public PostController(PostRepository postRepository, UserRepository userRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
    }

    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> postList = new ArrayList<>();

        this.postRepository
                .findAll()
                .forEach(post ->
                        postList.add(postMapper.toDTO(post)));

        return ResponseEntity.ok(postList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Integer id) {
        return postRepository.findById(id)
                .map(post -> {
                    PostDTO postDTO = postMapper.toDTO(post);
                    return ResponseEntity.ok(postDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    // TODO: Change this to get user info based on token
    @PostMapping("/{id}")
    public ResponseEntity<PostDTO> createPost(@PathVariable (name = "id") int id, @RequestBody PostDTO postDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post with provided id not found"));

        Post post = postMapper.toEntity(postDTO);
        post.setUser(user);
        Post savedPost = postRepository.save(post);

        PostDTO savedPostDTO = postMapper.toDTO(savedPost);
        return new ResponseEntity<>(savedPostDTO, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> updatePost(
            @PathVariable Integer id, @RequestBody PostDTO updatedPostDTO) {
        return postRepository.findById(id)
                .map(postToUpdate -> {
                    postToUpdate.setContent(updatedPostDTO.getContent());
                    postRepository.save(postToUpdate);
                    return new ResponseEntity<>(postMapper.toDTO(postToUpdate), HttpStatus.CREATED);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PostDTO> deletePost(@PathVariable Integer id) {
        return postRepository.findById(id)
                .map(post -> {
                    postRepository.delete(post);
                    return ResponseEntity.ok(postMapper.toDTO(post));
                }).orElse(ResponseEntity.notFound().build());
    }
}
