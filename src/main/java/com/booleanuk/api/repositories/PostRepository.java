package com.booleanuk.api.repositories;

import com.booleanuk.api.models.Post;
import com.booleanuk.api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer>{
    List<Post> findByUserUsername(String username);
}
