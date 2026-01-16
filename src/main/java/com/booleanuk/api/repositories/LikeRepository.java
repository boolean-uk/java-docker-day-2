package com.booleanuk.api.repositories;

import com.booleanuk.api.models.Like;
import com.booleanuk.api.models.Post;
import com.booleanuk.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    List<Like> findByPost(Post post);
    List<Like> findByPostAndUser(Post post, User user);
}
