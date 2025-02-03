package com.booleanuk.api.repositories;

import com.booleanuk.api.models.Follower;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowerRepository extends JpaRepository<Follower, Integer> {
}
