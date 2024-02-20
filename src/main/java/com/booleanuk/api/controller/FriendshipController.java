package com.booleanuk.api.controller;

import com.booleanuk.api.model.Friendship;
import com.booleanuk.api.repository.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friendships")
public class FriendshipController {

    @Autowired
    private FriendshipRepository friendships;

    @PostMapping
    public ResponseEntity<Friendship> createFriendship(@RequestBody Friendship friendship) {
        Friendship savedFriendship = friendships.save(friendship);
        return new ResponseEntity<>(savedFriendship, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Friendship>> getAllFriendshipsByUserId(@PathVariable int userId) {
        List<Friendship> friendshipList = friendships.findAllByUserId(userId);
        return new ResponseEntity<>(friendshipList, HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Friendship> deleteFriendship(@PathVariable int userId) {
        Friendship deletedFriendship = (Friendship) friendships.findByUserId(userId);
        if (deletedFriendship != null) {
            friendships.delete(deletedFriendship);
            return new ResponseEntity<>(deletedFriendship, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
