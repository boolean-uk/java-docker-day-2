package com.booleanuk.api.controllers;

import com.booleanuk.api.models.Post;
import com.booleanuk.api.models.User;
import com.booleanuk.api.repositories.PostRepository;
import com.booleanuk.api.responses.ErrorResponse;
import com.booleanuk.api.responses.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("posts")
public class PostController {
    @Autowired
    private PostRepository postRepository;
    private LocalDateTime today;


    @GetMapping
    public ResponseEntity<Response<?>> getAll(){
        Response<List<Post>> response = new Response<>();
        response.set(this.postRepository.findAll());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getOne(@PathVariable int id){
        Post post = findThePost(id);
        if (post == null){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Post not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        Response<Post> response = new Response<>();
        response.set(post);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Response<?>> create(@RequestBody Post post){
        today = LocalDateTime.now();
        if (post.getContent() == null){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Bad request");
            new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
        }
        Response<Post> response = new Response<>();
        post.setPostedAt(String.valueOf(today));
        response.set(this.postRepository.save(post));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> update(@PathVariable int id, @RequestBody Post post){
        today = LocalDateTime.now();
        Post updatePost = findThePost(id);
        if (updatePost == null){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Post not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        if (post.getContent() == null){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Bad request");
            new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
        }
        updatePost.setContent(post.getContent());
        updatePost.setUpdatedAt(String.valueOf(today));

        Response<Post> response = new Response<>();
        response.set(this.postRepository.save(updatePost));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> delete(@PathVariable int id){
        Post post = findThePost(id);
        if (post == null){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Post not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        Response<Post> response = new Response<>();
        this.postRepository.delete(post);
        response.set(post);
        return ResponseEntity.ok(response);
    }


    private Post findThePost(int id){
        return this.postRepository.findById(id)
                .orElse(null);
    }
}
