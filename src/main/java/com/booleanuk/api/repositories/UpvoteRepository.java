package com.booleanuk.api.repositories;

import com.booleanuk.api.models.Upvote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UpvoteRepository extends JpaRepository<Upvote, Integer> {
}
