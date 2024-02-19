package com.booleanuk.api.controllers;

import com.booleanuk.api.HelperUtils;
import com.booleanuk.api.models.Follow;
import com.booleanuk.api.models.Post;
import com.booleanuk.api.repositories.FollowRepository;
import com.booleanuk.api.repositories.PostRepository;
import com.booleanuk.api.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/followers")
public class FollowController {

    @Autowired
    FollowRepository followRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllFollowers() {
        List<Follow> followList = this.followRepository.findAll();
        return HelperUtils.okRequest(followList);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createFollow(@RequestBody Follow follow) {
        return HelperUtils.createdRequest(this.followRepository.save(follow));
    }
}
