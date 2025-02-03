package com.booleanuk.api.repositories;

import com.booleanuk.api.models.Upvote;
import com.booleanuk.api.models.UpvoteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpvoteRepository extends JpaRepository<Upvote, UpvoteId> {
}
