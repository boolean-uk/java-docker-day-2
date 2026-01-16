package com.booleanuk.api.controllers;

import com.booleanuk.api.models.Post;
import com.booleanuk.api.models.User;
import com.booleanuk.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("posts")
public class PostController extends ControllerTemplate<Post> {
    protected record ConstructPost(Integer byUser, String message) {
        public boolean hasNullFields() {
            return byUser == null || message == null;
        }
    }

    protected record EditPost(Integer id, String message) {
        public boolean hasNullFields() {
            return id == null || message == null;
        }
    }

    @Autowired
    private UserRepository userRepository;

    @PostMapping("send")
    public ResponseEntity<Post> makeAPost(@RequestBody ConstructPost request) {
        if (request.hasNullFields()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        User _user = userRepository.findById(request.byUser).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Post _newPost = new Post(request.message);

        _newPost.setUser(_user);
        _user.getPosts().add(_newPost);

        return new ResponseEntity<>(repository.save(_newPost), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Post> update(@RequestBody final EditPost request) {
        if (request.hasNullFields()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        Post _post = repository.findById(request.id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found."));

        _post.setMessage(request.message);
        _post.setEdited(true);

        return new ResponseEntity<>(repository.save(_post), HttpStatus.CREATED);
    }
}
