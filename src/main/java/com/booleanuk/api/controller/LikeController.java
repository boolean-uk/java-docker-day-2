package com.booleanuk.api.controller;

import com.booleanuk.api.model.Like;
import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.LikeRepository;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("likes")
public class LikeController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikeRepository likeRepository;

    //Get all likes of a post
    @GetMapping("{likeId}/posts{postId}")
    public ResponseEntity<Response<?>> getAllPostLikes(@PathVariable int likeId, @PathVariable int postId){
        Like like = this.likeRepository.findById(likeId).orElse(null);
        Post post = this.postRepository.findById(postId).orElse(null);

        if (post == null || like == null){
            ErrorResponse error = new ErrorResponse();
            error.set("No post with that id found");

            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        LikeResponse response = new LikeResponse();
        this.likeRepository.findById(like.getId());

        return ResponseEntity.ok(response);
    }

    //Add like
    @PostMapping("/post/{postId}")
    public ResponseEntity<Response<?>> createLikeForPost(@PathVariable int postId){

        Post post =  this.postRepository.findById(postId).orElse(null);
        if (post == null){
            ErrorResponse error = new ErrorResponse();
            error.set("No post with that id found");

            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        LikeResponse response = new LikeResponse();

        Like like = new Like(post.getUser().getId());
        this.likeRepository.save(like);

        response.set(like);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //Remove Like
    @DeleteMapping("{likeId}/posts/{postId}")
    public ResponseEntity<Response<?>> deletePost(@PathVariable int likeId, @PathVariable int postId){

        Like likeToDelete = this.likeRepository.findById(likeId).orElse(null);
        if (likeToDelete == null){
            ErrorResponse error = new ErrorResponse();
            error.set("No like with that id found");

            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        LikeResponse response = new LikeResponse();

        this.likeRepository.delete(likeToDelete);
        response.set(likeToDelete);

        return ResponseEntity.ok(response);
    }

}
