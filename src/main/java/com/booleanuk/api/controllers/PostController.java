package com.booleanuk.api.controllers;

import com.booleanuk.api.HelperUtils;
import com.booleanuk.api.models.Post;
import com.booleanuk.api.repositories.PostRepository;
import com.booleanuk.api.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    PostRepository postRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllPosts() {
        List<Post> postList = this.postRepository.findAll();
        return HelperUtils.okRequest(postList);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createPost(@RequestBody Post post) {
        return HelperUtils.createdRequest(this.postRepository.save(post));
    }
}
