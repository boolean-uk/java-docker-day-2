package com.booleanuk.api.services;

import com.booleanuk.api.models.Post;
import com.booleanuk.api.models.User;
import com.booleanuk.api.repositories.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PostService {
    private PostRepository postRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(UUID postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public List<Post> getPostsByUser(User user) {
        return postRepository.findAllByUser(user);
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Post updatePost(UUID postId, Post updatedPost) {
        Optional<Post> existingPostOptional = postRepository.findById(postId);

        if (existingPostOptional.isPresent()) {
            Post existingPost = existingPostOptional.get();
            existingPost.setContent(updatedPost.getContent());
            existingPost.setUpdatedAt(ZonedDateTime.now());
            return postRepository.save(existingPost);
        }

        return null;
    }

    public void deletePost(UUID postId) {
        postRepository.deleteById(postId);
    }
}
