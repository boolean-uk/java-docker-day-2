package com.booleanuk.api.repository;

import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.Reaction;
import com.booleanuk.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository extends JpaRepository<Reaction, Integer> {
    public Reaction findByByUserAndPost(User byUser, Post post);
}
