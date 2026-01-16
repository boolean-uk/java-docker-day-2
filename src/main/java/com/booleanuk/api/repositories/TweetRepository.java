package com.booleanuk.api.repositories;

import com.booleanuk.api.models.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TweetRepository extends JpaRepository<Tweet, Integer> {
}
