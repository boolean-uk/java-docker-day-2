package com.booleanuk.api.repositories;

import com.booleanuk.api.models.Follower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowerRepository extends JpaRepository<Follower, Integer> {
    List<Follower> findAllByFollowingId(int followingId);
}
