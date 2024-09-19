package com.booleanuk.api.controller;

import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.dto.PostDTO;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.responses.ErrorResponse;
import com.booleanuk.api.responses.PostListResponse;
import com.booleanuk.api.responses.PostResponse;
import com.booleanuk.api.responses.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("posts")
public class PostController {

    @Autowired
    private PostRepository posts;

    @GetMapping
    public ResponseEntity<PostListResponse> getAll() {
        PostListResponse postListResponse = new PostListResponse();
        postListResponse.set(this.posts.findAll());
        return new ResponseEntity<>(postListResponse, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Response<?>> getOne(@PathVariable int id) {
        Post toReturn = this.posts.findById(id).orElse(null);
        if (toReturn == null) {
            ErrorResponse errorResponse = new ErrorResponse("not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        PostResponse postResponse = new PostResponse();
        postResponse.set(toReturn);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PostResponse> create(@RequestBody PostDTO toAdd) {

        Post actualToAdd = new Post();
        actualToAdd.setTitle(toAdd.getTitle());
        actualToAdd.setContent(toAdd.getContent());
        actualToAdd.setPostedOn(LocalDateTime.now());
        actualToAdd.setLastUpdatedOn(LocalDateTime.now());

        PostResponse postResponse = new PostResponse();
        postResponse.set(this.posts.save(actualToAdd));
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Response<?>> delete(@PathVariable int id) {
        Post toDelete = this.posts.findById(id).orElse(null);
        if (toDelete == null) {
            ErrorResponse errorResponse = new ErrorResponse("not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        this.posts.delete(toDelete);

        PostResponse postResponse = new PostResponse();
        postResponse.set(toDelete);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Response<?>> update(@PathVariable int id, @RequestBody PostDTO newData) {
        Post toUpdate = this.posts.findById(id).orElse(null);
        if (toUpdate == null) {
            ErrorResponse errorResponse = new ErrorResponse("not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        toUpdate.setTitle(newData.getTitle());
        toUpdate.setContent(newData.getContent());
        toUpdate.setLastUpdatedOn(LocalDateTime.now());

        PostResponse postResponse = new PostResponse();
        postResponse.set(this.posts.save(toUpdate));
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

}
