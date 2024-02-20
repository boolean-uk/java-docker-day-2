package com.booleanuk.api.controller;

import com.booleanuk.api.exceptions.CustomDataNotFoundException;
import com.booleanuk.api.exceptions.CustomParamaterConstraintException;
import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.UserRepository;
import com.booleanuk.api.response.Response;
import com.booleanuk.api.response.SuccessResponse;
import com.booleanuk.api.util.DateCreater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("users/{id}/posts")
public class PostController {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Response<?>> createPost(@PathVariable (name = "id") int id, @RequestBody Post post) {
        User user = this.getAUser(id);

        checkValidInput(user);

        Post post1 = new Post(post.getContent(), DateCreater.getCurrentDate(), DateCreater.getCurrentDate(), user);
        if(post1.getLikes() == null || post1.getLikes().isEmpty()) {
            post1.setLikes(new ArrayList<>());
        }
        checkValidInput(post1);
        this.postRepository.save(post1);


        return new ResponseEntity<>(new SuccessResponse(post1), HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<Post>> getPost(@PathVariable (name = "id") int id) {
        User user = this.getAUser(id);

        if(user.getPosts() == null) {
            user.setPosts(new ArrayList<>());
        }

        List<Post> posts = user.getPosts();

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }



    @GetMapping("/{postId}")
    public ResponseEntity<Response<?>> getSpecificPost(@PathVariable(name = "id") int id, @PathVariable (name = "postId") int postId) {
        User user = this.getAUser(id);
        Post post = user.getPosts().get(postId);

        this.checkValidInput(post);

        return new ResponseEntity<>(new SuccessResponse(post), HttpStatus.OK);

    }

    @PutMapping("/{postId}")
    public ResponseEntity<Response<?>> updatePost(@PathVariable (name = "id") int id,
                                                  @PathVariable (name = "postId") int postId,
                                                  @RequestBody Post post) {

        User user = getAUser(id);
        user.setUpdatedAt(DateCreater.getCurrentDate());

        Post post1 = user.getPosts().get(postId);

        checkValidInput(post1);

        post1.setUpdatedAt(DateCreater.getCurrentDate());
        post1.setContent(post.getContent());

        return new ResponseEntity<>(new SuccessResponse(post1), HttpStatus.CREATED);

    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Response<?>> deletePost(@PathVariable (name = "id") int id, @PathVariable (name = "postId") int postId) {
        User user = this.getAUser(id);
        Post post = user.getPosts().get(postId);

        checkValidInput(post);

        user.getPosts().remove(post);
        this.postRepository.delete(post);

        return new ResponseEntity<>(new SuccessResponse(post), HttpStatus.OK);

    }


    @GetMapping("/getAllPosts")
    public ResponseEntity<List<Post>> getAllPosts(@PathVariable (name = "id") int id) {

        List<Post> posts = this.postRepository.findAll();
        System.out.println(posts.get(0).getLikes());
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }


    @PutMapping("/getAllPosts/{postId}/like")
    public ResponseEntity<Response<?>> likePost(@PathVariable (name = "id") int id, @PathVariable (name = "postId") int postId) {
        User user = this.getAUser(id);
        Post post = this.getAPost(postId);

        this.checkValidInput(user);
        this.checkValidInput(post);

        if(user.getPostsList() == null) {
            user.setPostsList(new ArrayList<>());
        }

        if(post.getLikes() == null) {
            post.setLikes(new ArrayList<>());
        }

        post.getLikes().add(user);
        user.getPostsList().add(post);

        this.postRepository.save(post);
        this.userRepository.save(user);


        return new ResponseEntity<>(new SuccessResponse(post), HttpStatus.CREATED);
    }


    private User getAUser(int id) {
        return this.userRepository.findById(id).orElseThrow(() -> new CustomDataNotFoundException("No user with that ID found"));
    }


    private Post getAPost(int id) {
        return this.postRepository.findById(id).orElseThrow(() -> new CustomDataNotFoundException("No user with that ID found"));
    }

    private void checkValidInput(Post post) {
        if(post.getCreatedAt() == null || post.getContent() == null || post.getUpdatedAt() == null) {
            throw new CustomParamaterConstraintException("Bad request");
        }
    }

    private void checkValidInput(User user) {
        if(user.getCreatedAt() == null || user.getUsername() == null || user.getUpdatedAt() == null || user.getEmail() == null || user.getPassword() == null) {
            throw new CustomParamaterConstraintException("Bad request");
        }
    }



}
