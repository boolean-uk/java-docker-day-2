package com.booleanuk.api.controllers;

import com.booleanuk.api.models.Relation;
import com.booleanuk.api.models.User;
import com.booleanuk.api.payload.response.CustomResponse;
import com.booleanuk.api.repository.RelationRepository;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RelationRepository relationRepository;

    @PostMapping("/{id}/add-friend/{friendId}")
    public ResponseEntity<CustomResponse> addFriend(@PathVariable int id, @PathVariable int friendId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found"));

        user.getRelations().add(relationRepository.save(new Relation(user, friend)));
        userRepository.save(user);

        CustomResponse customResponse = new CustomResponse("success", user);
        return ResponseEntity.ok(customResponse);
    }
}
