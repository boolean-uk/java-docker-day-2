package com.booleanuk.api.library.repository;

import com.booleanuk.api.library.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
