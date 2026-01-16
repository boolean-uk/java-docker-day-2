package com.booleanuk.api.repository;

import com.booleanuk.api.model.Follower;
import com.booleanuk.api.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowerRepository extends JpaRepository<Follower, Integer> {
    List<Follower> findAllIsFollowingByIsFollowed(User user);
    List<Follower> findAllIsFollowedByIsFollowing(User user);
}
