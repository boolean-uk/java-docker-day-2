package com.booleanuk.api.controller;

import com.booleanuk.api.payload.response.Response;
import com.booleanuk.api.repository.UserFollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("follows")
public class UserFollowController {
    @Autowired
    UserFollowRepository repository;

    @PostMapping("/{followId}/users/{userId}")
    public ResponseEntity<Response<?>> followUserForUser() {return null;}

    @DeleteMapping("/{followId}/users/{userId}")
    public ResponseEntity<Response<?>> unfollowUserForUser() {return null;}
}
