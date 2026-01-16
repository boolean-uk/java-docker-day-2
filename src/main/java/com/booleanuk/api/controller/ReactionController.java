package com.booleanuk.api.controller;

import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.Reaction;
import com.booleanuk.api.model.User;
import com.booleanuk.api.model.dto.ReactionDTO;
import com.booleanuk.api.repository.PostRepository;
import com.booleanuk.api.repository.ReactionRepository;
import com.booleanuk.api.repository.UserRepository;
import com.booleanuk.api.responses.ErrorResponse;
import com.booleanuk.api.responses.ReactionResponse;
import com.booleanuk.api.responses.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("reactions")
public class ReactionController {

    @Autowired
    private PostRepository posts;

    @Autowired
    private UserRepository users;

    @Autowired
    private ReactionRepository reactions;

    @PostMapping
    public ResponseEntity<Response<?>> addReaction(@RequestBody ReactionDTO request) {
        User byUser = this.users.findById(request.getByUser()).orElse(null);
        Post onPost = this.posts.findById(request.getPost()).orElse(null);

        if (byUser == null || onPost == null) {
            ErrorResponse errorResponse = new ErrorResponse("user or post not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // assuming user can only make one reaction per post
        if (this.reactions.findByByUserAndPost(byUser, onPost) != null) {
            ErrorResponse errorResponse = new ErrorResponse("user already have a reaction on post");
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        Reaction reaction = new Reaction();
        reaction.setReaction(request.getReaction());
        reaction.setPost(onPost);
        reaction.setByUser(byUser);

        onPost.addReaction(reaction);
        this.posts.save(onPost);

        ReactionResponse reactionResponse = new ReactionResponse();
        reactionResponse.set(this.reactions.save(reaction));
        return new ResponseEntity<>(reactionResponse, HttpStatus.CREATED);
    }

}
