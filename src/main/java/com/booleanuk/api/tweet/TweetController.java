package com.booleanuk.api.tweet;

import com.booleanuk.api.user.User;
import com.booleanuk.api.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("tweets")
public class TweetController {

    @Autowired
    private TweetRepository tweetRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Tweet>> getAll(){
        return ResponseEntity.ok(tweetRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<Tweet>> getAllTweetsFromUser(@PathVariable int userId){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found")
        );

        return ResponseEntity.ok(user.getTweets());
    }

    @PostMapping
    public ResponseEntity<Tweet> createTweet(@RequestBody Tweet tweet){
        User user = userRepository.findById(tweet.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        tweet.setUser(user);

        return new ResponseEntity<Tweet>(this.tweetRepository.save(tweet), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tweet> updateTweet(@PathVariable int id, @RequestBody Tweet tweet){
        Tweet tweetToUpdate = tweetRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found")
        );

        tweetToUpdate.setMessage(tweet.getMessage());
        return new ResponseEntity<Tweet>(this.tweetRepository.save(tweetToUpdate), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Tweet> deleteTweet(@PathVariable int id){
        Tweet tweetToDelete = this.tweetRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found")
        );
        this.tweetRepository.delete(tweetToDelete);
        return ResponseEntity.ok(tweetToDelete);
    }
}
