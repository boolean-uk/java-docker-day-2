package com.booleanuk.api.controller;


import ch.qos.logback.core.spi.PreSerializationTransformer;
import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.UserRepository;
import com.booleanuk.api.response.ErrorResponse;
import com.booleanuk.api.response.PostListResponse;
import com.booleanuk.api.response.PostResponse;
import com.booleanuk.api.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;


    //Get all posts a user has made (Makes no sense to get all posts from everyone in a social media setting)
    @GetMapping("/users/{userId}")
    public ResponseEntity<Response<?>> getAllUserPosts(@PathVariable int userId){

        User user = this.userRepository.findById(userId).orElse(null);

        if (user == null){
            ErrorResponse error = new ErrorResponse();
            error.set("No user with that id found");

            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        List<Post> postList = this.postRepository.findAllByUserId(user.getId());

        PostListResponse response = new PostListResponse();
        response.set(postList);

        return ResponseEntity.ok(response);
    }


    //Creates a post for a user. No posts can be made without user.
    @PostMapping("/users/{userId}")
    public ResponseEntity<Response<?>> createUserPost(@PathVariable int userId, @RequestBody Post post){

        User user = this.userRepository.findById(userId).orElse(null);
        if (user == null){
            ErrorResponse error = new ErrorResponse();
            error.set("No user with that id found");

            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        if (post.getUserComment() == null || post.getImage() == null){
            ErrorResponse error = new ErrorResponse();
            error.set("A post needs to be an image or comment");

            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        PostResponse response = new PostResponse();

        user.addPost(post);
        post.setUser(user);

        this.postRepository.save(post);
        response.set(post);

        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    //Changes an already made post from a user
    @PutMapping("{postId}/users/{userId}")
    public ResponseEntity<Response<?>> updateStudent(@PathVariable int postId, @PathVariable int userId, @RequestBody Post post){
        if (post.getUserComment() == null || post.getImage() == null){
            ErrorResponse error = new ErrorResponse();
            error.set("A post needs to be an image or comment");

            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        Post postToUpdate = this.postRepository.findById(postId).orElse(null);
        User user = this.userRepository.findById(userId).orElse(null);

        if (postToUpdate == null || user == null){
            ErrorResponse error = new ErrorResponse();
            error.set("No post or user with that id found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        user.removePost(postToUpdate);

        postToUpdate.setImage(post.getImage());
        postToUpdate.setUserComment(post.getUserComment());
        postToUpdate.setLikes(post.getLikes());
        postToUpdate.setShares(post.getShares());
        postToUpdate.setComments(post.getComments());

        user.addPost(postToUpdate);
        this.postRepository.save(postToUpdate);

        PostResponse response = new PostResponse();
        response.set(postToUpdate);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //Deletes a users post
    @DeleteMapping("{postId}/users/{userId}")
    public ResponseEntity<Response<?>> deletePost(@PathVariable int postId, @PathVariable int userId){

        Post postToDelete = this.postRepository.findById(postId).orElse(null);
        User user = this.userRepository.findById(userId).orElse(null);

        if (postToDelete == null || user == null){
            ErrorResponse error = new ErrorResponse();
            error.set("No post or user with that id found");

            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        PostResponse response = new PostResponse();

        user.removePost(postToDelete);
        this.postRepository.delete(postToDelete);

        response.set(postToDelete);

        return ResponseEntity.ok(response);
    }

}
