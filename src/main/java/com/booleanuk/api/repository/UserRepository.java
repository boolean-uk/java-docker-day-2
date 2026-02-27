package com.booleanuk.api.repository;

import com.booleanuk.api.model.Tweet;
import com.booleanuk.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<Tweet> findByUser(User user);
}
