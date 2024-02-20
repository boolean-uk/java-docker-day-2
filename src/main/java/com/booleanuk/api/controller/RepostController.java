package com.booleanuk.api.controller;

import com.booleanuk.api.model.Repost;
import com.booleanuk.api.repository.RepostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reposts")
public class RepostController {

    @Autowired
    private RepostRepository reposts;

    @PostMapping
    public ResponseEntity<Repost> createRepost(@RequestBody Repost repost) {
        Repost savedRepost = reposts.save(repost);
        return new ResponseEntity<>(savedRepost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<List<Repost>> getAllRepostsByPostId(@PathVariable int postId) {
        List<Repost> repostList = reposts.findAllByPostId(postId);
        return new ResponseEntity<>(repostList, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Repost> deleteRepost(@PathVariable int postId) {
        Repost deletedRepost = (Repost) reposts.findByPostId(postId);
        if (deletedRepost != null) {
            reposts.delete(deletedRepost);
            return new ResponseEntity<>(deletedRepost, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
