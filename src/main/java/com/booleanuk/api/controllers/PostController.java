package com.booleanuk.api.controllers;

import com.booleanuk.api.models.Post;
import com.booleanuk.api.models.User;
import com.booleanuk.api.repositories.PostRepository;
import com.booleanuk.api.repositories.UserRepository;
import com.booleanuk.api.responses.ErrorResponse;
import com.booleanuk.api.responses.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("posts")
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;
    private LocalDateTime today;

    //get all posts
    @GetMapping
    public ResponseEntity<Response<?>> getAll(){
        Response<List<Post>> response = new Response<>();
        response.set(this.postRepository.findAll());
        return ResponseEntity.ok(response);
    }
    //get all posts from spesific user
    @GetMapping("/user")
    public ResponseEntity<Response<?>> getAllPostsPerUser(@RequestParam String username){
        User user = this.userRepository.findByUsername(username).orElse(null);
        if (user == null){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("User does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        Response<List<Post>> response = new Response<>();
        response.set(user.getPosts());
        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<Response<?>> create(@RequestBody Post post,
                                              @RequestParam int user_id){
        today = LocalDateTime.now();
        User user = this.userRepository.findById(user_id)
                .orElse(null);

        if (user == null){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("User does not exist");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        if (post.getContent() == null){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.set("Bad request");
            new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
        }
        Response<Post> response = new Response<>();
        post.setPostedAt(String.valueOf(today));
        post.setUser(user);
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
