package com.booleanuk.api.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("posts")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Post> getAllPosts() {
        return this.postRepository.findAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("{id}")
    public Post getOnePost(@PathVariable int id) {
        return this.postRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found.")
        );
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Post createPost(@RequestBody Post post) {

        Post newPost = new Post(post.getText());
        return this.postRepository.save(newPost);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("{id}")
    public Post updatePost(@PathVariable int id, @RequestBody Post post) {
        Post postToUpdate = this.postRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found")
        );

        postToUpdate.setText(post.getText());
        return this.postRepository.save(postToUpdate);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("{id}")
    public Post deletePost(@PathVariable int id) {
        Post postToDelete = this.postRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found.")
        );

        this.postRepository.delete(postToDelete);
        return postToDelete;
    }
}