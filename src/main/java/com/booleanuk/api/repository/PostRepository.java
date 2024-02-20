package com.booleanuk.api.repository;

import com.booleanuk.api.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByUserId(int userId);

    List<Post> findAllByUserId(int userId);
}