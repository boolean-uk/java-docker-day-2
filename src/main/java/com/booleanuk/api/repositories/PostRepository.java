package com.booleanuk.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.booleanuk.api.models.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
