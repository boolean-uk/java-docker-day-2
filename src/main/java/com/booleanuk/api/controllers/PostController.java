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

import java.util.List;

@RestController
@RequestMapping("posts")
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping

    public ResponseEntity<Post> createPost(@RequestBody Post post){


        try {

            return new ResponseEntity<Post>(this.postRepository.save(post),
                    HttpStatus.CREATED);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not create post, please check all required fields are correct.");
        }


    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(){
        return ResponseEntity.ok(this.postRepository.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Post> getOnePost(@PathVariable int id){
        Post post=this.postRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
        return ResponseEntity.ok(post);
    }

    @PutMapping("{id}")
    public ResponseEntity<Post> updatePost(@PathVariable int id,
                                               @RequestBody Post post){
        Post postToUpdate=this.postRepository.findById(id).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No post with that ID found")
        );
        postToUpdate.setLikes(post.getLikes());
        postToUpdate.setMessage(post.getMessage());
        return new ResponseEntity<Post>(this.postRepository.save(postToUpdate
        ), HttpStatus.CREATED);



    }



    @DeleteMapping("{id}")
    public ResponseEntity<Post> deletePost(@PathVariable int id){
        Post postToDelete=this.postRepository.findById(id).orElseThrow(
                ()->new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No post with that ID found")
        );

        this.postRepository.delete(postToDelete);
        return ResponseEntity.ok(postToDelete);
    }
}
