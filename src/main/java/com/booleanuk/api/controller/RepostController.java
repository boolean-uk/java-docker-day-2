package com.booleanuk.api.controller;

import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.Repost;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.RepostRepository;
import com.booleanuk.api.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("reposts")
public class RepostController {
    @Autowired
    private RepostRepository repostRepository;

    @Autowired
    private PostRepository postRepository;

    private ErrorResponse errorResponse = new ErrorResponse();
    private RepostResponse repostResponse = new RepostResponse();
    private RepostListResponse repostListResponse = new RepostListResponse();

    @GetMapping
    public ResponseEntity<Response<?>> getAllReposts() {
        this.repostListResponse.set(this.repostRepository.findAll());
        return ResponseEntity.ok(repostListResponse);
    }

    @PostMapping
    public ResponseEntity<Response<?>> createRepost(@RequestBody Repost repost) {
        repost.setReposted(LocalDateTime.now());
        try {
            this.repostResponse.set(this.repostRepository.save(repost));
        } catch (Exception e) {
            this.errorResponse.set("Bad request");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(repostResponse, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Response<?>> getOneRepost(@PathVariable int id) {
        Repost repost = this.repostRepository.findById(id).orElse(null);
        if (repost == null) {
            this.errorResponse.set("Not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        this.repostResponse.set(repost);
        return ResponseEntity.ok(repostResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Response<?>> deleteRepost(@PathVariable int id) {
        Repost repostToDelete = this.repostRepository.findById(id).orElse(null);
        if (repostToDelete == null) {
            this.errorResponse.set("Not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        this.repostRepository.delete(repostToDelete);
        this.repostResponse.set(repostToDelete);
        return ResponseEntity.ok(repostResponse);
    }
}
