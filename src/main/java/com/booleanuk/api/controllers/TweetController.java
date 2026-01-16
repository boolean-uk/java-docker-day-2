package com.booleanuk.api.controllers;

import com.booleanuk.api.models.Tweet;
import com.booleanuk.api.models.Profile;
import com.booleanuk.api.repositories.TweetRepository;
import com.booleanuk.api.repositories.ProfileRepository;
import com.booleanuk.api.responses.ErrorResponse;
import com.booleanuk.api.responses.Response;
import com.booleanuk.api.responses.TweetListResponse;
import com.booleanuk.api.responses.TweetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("tweets")
public class TweetController {
    @Autowired
    private TweetRepository tweetRepository;
    @Autowired
    private ProfileRepository profileRepository;

    @GetMapping
    public ResponseEntity<TweetListResponse> getAllTweets() {
        TweetListResponse tweetListResponse = new TweetListResponse();
        tweetListResponse.set(this.tweetRepository.findAll());
        return ResponseEntity.ok(tweetListResponse);
    }

    @PostMapping("/profile_id_{profile_id}")
    public ResponseEntity<Response<?>> createTweet(@PathVariable int profile_id, @RequestBody Tweet tweet) {
        TweetResponse tweetResponse = new TweetResponse();
        if (!this.profileRepository.existsById(profile_id)) {
            ErrorResponse error = new ErrorResponse();
            error.set("Not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        try {
            tweet.setProfile(this.profileRepository.getReferenceById(profile_id));
            tweetResponse.set(this.tweetRepository.save(tweet));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(tweetResponse, HttpStatus.CREATED);
    }

    @GetMapping("/profile_id_{profile_id}")
    public ResponseEntity<Response<?>> getTweetsForAProfile(@PathVariable int profile_id) {
        Profile profile = this.profileRepository.findById(profile_id).orElse(null);
        if (profile == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        List<Tweet> allSpecifiedTweets = new ArrayList<>();
        for (Tweet tweet : this.tweetRepository.findAll()) {
            if (tweet.getProfile().getId() == profile_id) {
                allSpecifiedTweets.add(tweet);
            }
        }
        TweetListResponse tweetListResponse = new TweetListResponse();
        tweetListResponse.set(allSpecifiedTweets);
        return ResponseEntity.ok(tweetListResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response<?>> updateTweet(@PathVariable int id, @RequestBody Tweet tweet) {
        Tweet tweetToUpdate = this.tweetRepository.findById(id).orElse(null);
        if (tweetToUpdate == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        tweetToUpdate.setPost(tweet.getPost());
        tweetToUpdate.setCreatedAt(tweet.getCreatedAt());
        tweetToUpdate.setLikes(tweet.getLikes());
        tweetToUpdate.setRetweets(tweet.getRetweets());
        tweetToUpdate.setProfile(tweet.getProfile());
        try {
            tweetToUpdate = this.tweetRepository.save(tweetToUpdate);
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse();
            error.set("Bad request");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        TweetResponse tweetResponse = new TweetResponse();
        tweetResponse.set(tweetToUpdate);
        return new ResponseEntity<>(tweetResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteTweet(@PathVariable int profile_id, @PathVariable int id) {
        Tweet tweetToDelete = this.tweetRepository.findById(id).orElse(null);
        if (tweetToDelete == null) {
            ErrorResponse error = new ErrorResponse();
            error.set("not found");
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        this.tweetRepository.delete(tweetToDelete);
        TweetResponse tweetResponse = new TweetResponse();
        tweetResponse.set(tweetToDelete);
        return ResponseEntity.ok(tweetResponse);
    }
}
