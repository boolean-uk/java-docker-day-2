package com.booleanuk.library.repository;

import com.booleanuk.library.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
