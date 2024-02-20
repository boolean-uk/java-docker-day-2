package com.booleanuk.api.repository;

import com.booleanuk.api.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CommentRepository extends JpaRepository<Comment, Integer> {
}