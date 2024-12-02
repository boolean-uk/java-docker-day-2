package com.booleanuk.api.repository;

import com.booleanuk.api.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer>{
}
