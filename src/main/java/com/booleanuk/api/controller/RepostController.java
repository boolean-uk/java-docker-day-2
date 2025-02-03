package com.booleanuk.api.controller;

import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.User;
import com.booleanuk.api.model.dto.RepostDTO;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.UserRepository;
import com.booleanuk.api.responses.ErrorResponse;
import com.booleanuk.api.responses.Response;
import com.booleanuk.api.responses.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("reposts")
public class RepostController {

    @Autowired
    private UserRepository users;

    @Autowired
    private PostRepository posts;

    @PostMapping
    public ResponseEntity<Response<?>> makeRepost(@RequestBody RepostDTO request) {
        User byUser = this.users.findById(request.getByUser()).orElse(null);
        Post post = this.posts.findById(request.getPost()).orElse(null);

        if (byUser == null || post == null) {
            ErrorResponse errorResponse = new ErrorResponse("user or post not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // TODO: check if user already reposted this post

        byUser.addRepost(post);
        this.users.save(byUser);

        SuccessResponse successResponse = new SuccessResponse("repost done");
        return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
    }

}
