package com.booleanuk.api.repositories;


import com.booleanuk.api.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findAllByUserId(int userId);
}
