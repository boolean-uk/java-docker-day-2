package com.booleanuk.api.repository;

import com.booleanuk.api.model.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFollowRepository extends JpaRepository<UserFollow, Integer> {
}
