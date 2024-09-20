package com.booleanuk.api.repositories;

import com.booleanuk.api.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
