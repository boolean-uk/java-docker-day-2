package com.booleanuk.api.controller;

import com.booleanuk.api.payload.response.Response;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    UserRepository repository;

    @GetMapping("/feed/{id}")
    public ResponseEntity<Response<?>> getFeed() {return null;}

    @GetMapping("/profile/{Id}")
    public ResponseEntity<Response<?>> getUserProfile() {return null;}

}
