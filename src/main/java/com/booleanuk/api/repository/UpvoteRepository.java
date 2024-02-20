package com.booleanuk.api.repository;

import com.booleanuk.api.model.Upvote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UpvoteRepository extends JpaRepository<Upvote, Integer> {
    List<Upvote> findByPostId(int postId);

    List<Upvote> findAllByPostId(int postId);
}