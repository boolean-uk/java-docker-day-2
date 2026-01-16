package com.booleanuk.api.controller;

import com.booleanuk.api.model.Upvote;
import com.booleanuk.api.repository.UpvoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/upvotes")
public class UpvoteController {

    @Autowired
    private UpvoteRepository upvotes;

    @PostMapping
    public ResponseEntity<Upvote> createUpvote(@RequestBody Upvote upvote) {
        Upvote savedUpvote = upvotes.save(upvote);
        return new ResponseEntity<>(savedUpvote, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<Upvote>> getAllUpvotesByPostId(@PathVariable int postId) {
        List<Upvote> upvotesList = upvotes.findAllByPostId(postId);
        return new ResponseEntity<>(upvotesList, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Upvote> deleteUpvote(@PathVariable int postId) {
        Upvote deletedUpvote = (Upvote) upvotes.findByPostId(postId);
        if (deletedUpvote != null) {
            upvotes.delete(deletedUpvote);
            return new ResponseEntity<>(deletedUpvote, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
