package com.booleanuk.api.repository;

import com.booleanuk.api.model.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserFollowRepository extends JpaRepository<UserFollow, Integer> {
    List<UserFollow> findByFollowId(int followId);

    List<UserFollow> findByUserId(int userId);

    @Query("SELECT COUNT(*) FROM UserFollow uf JOIN uf.follow f WHERE f.id = :followId")
    int findAmountOfFollowers(int followId);

    @Query("SELECT COUNT(*) FROM UserFollow uf JOIN uf.user u WHERE u.id = :userId")
    int findAmountOfFollowing(int userId);

    boolean existsByUserIdAndFollowId(int userId, int followId);

    Optional<UserFollow> findByUserIdAndFollowId(int userId, int followId);
}
