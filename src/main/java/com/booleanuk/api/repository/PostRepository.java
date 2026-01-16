package com.booleanuk.api.repository;

import com.booleanuk.api.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<List<Post>> findByUserId(int userId);
}
