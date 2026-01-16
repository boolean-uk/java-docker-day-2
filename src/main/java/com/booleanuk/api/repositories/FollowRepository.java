package com.booleanuk.api.repositories;

import com.booleanuk.api.models.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Integer> {
}
