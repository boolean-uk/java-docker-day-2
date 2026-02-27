package com.booleanuk.api.controller;

import com.booleanuk.api.model.Tweet;
import com.booleanuk.api.model.User;
import com.booleanuk.api.repository.TweetRepository;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("tweets")
public class TweetController {
    @Autowired
    private TweetRepository tweetRepository;
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<Iterable<Tweet>> getTweets() {
        Iterable<Tweet> tweets = tweetRepository.findAll();
        return ResponseEntity.ok(tweets);
    }

    @PostMapping
    public ResponseEntity<Tweet> createTweet(@PathVariable int userId, @RequestBody Tweet tweet) {

        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No User with that ID found")
        );
        return new ResponseEntity<>(this.tweetRepository.save(tweet), HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Tweet> getTweetById(@PathVariable int id) {
        Tweet tweet = null;
        tweet = this.tweetRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Tweet with that ID found")
        );
        return ResponseEntity.ok(tweet);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Tweet> deleteTweet(@PathVariable int id) {
        Tweet tweet = this.tweetRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Tweet with that ID found")
        );
        this.tweetRepository.delete(tweet);
        return ResponseEntity.ok(tweet);
    }

    @PutMapping("{id}")
    public ResponseEntity<Tweet> updateTweet(@PathVariable int id, @RequestBody Tweet tweet) {
        Tweet tweetToUpdate = this.tweetRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No Tweet with that ID found")
        );
        tweet.setId(tweetToUpdate.getId());
        return new ResponseEntity<>(this.tweetRepository.save(tweet), HttpStatus.CREATED);
    }


}
