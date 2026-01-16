package com.booleanuk.api.likes;

import com.booleanuk.api.exceptions.BadRequestException;
import com.booleanuk.api.exceptions.NotFoundException;
import com.booleanuk.api.posts.Post;
import com.booleanuk.api.posts.PostRepository;
import com.booleanuk.api.responses.Response;
import com.booleanuk.api.users.User;
import com.booleanuk.api.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{userId}/posts/{postId}/likes")
public class LikeController {

    @Autowired
    LikeRepository likeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PostRepository postRepository;

    @PostMapping()
    public ResponseEntity<?> create( @PathVariable int userId, @PathVariable int postId) {

        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("No User with that id: " + userId + " found")
        );

        Post post = this.postRepository.findById(postId).orElseThrow(
                () -> new NotFoundException("No Post with that id: " + postId + " found")
        );
        try{
            Like like = new Like();
            post.setLikesCount(post.getLikesCount() + 1);
            postRepository.save(post);
            like.setUser(user);
            like.setPost(post);
            this.likeRepository.save(like);
            Response<Like> response = new Response<>(
                    "success",
                    like);

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            throw new BadRequestException("bad request");
        }

    }

    @DeleteMapping({"{id}"})
    public ResponseEntity<?> delete( @PathVariable int id, @PathVariable int userId, @PathVariable int postId) {
        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("No User with that id: " + userId + " found")
        );

        Post post = this.postRepository.findById(postId).orElseThrow(
                () -> new NotFoundException("No Post with that id: " + postId + " found")
        );

       Like like = this.likeRepository.findById(id).orElseThrow(
               () -> new NotFoundException("Like not found with ID " + id)
       );
        try{
            post.setLikesCount(post.getLikesCount() - 1);
           likeRepository.delete(like);
            Response<Like> response = new Response<>(
                    "success",
                    like);

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            throw new BadRequestException("bad request");
        }

    }

}
