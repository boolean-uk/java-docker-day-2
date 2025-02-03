package com.booleanuk.api.repositories;


import com.booleanuk.api.models.Upvote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UpvoteRepository extends JpaRepository<Upvote, Integer> {
    List<Upvote> findAllByUserId(int userId);
}
