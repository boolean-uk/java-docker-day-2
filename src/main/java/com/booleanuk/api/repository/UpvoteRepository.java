package com.booleanuk.api.repository;

import com.booleanuk.api.model.Upvote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpvoteRepository extends JpaRepository<Upvote, Integer> {
}
