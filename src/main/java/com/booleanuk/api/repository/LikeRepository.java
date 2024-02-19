package com.booleanuk.api.repository;

import com.booleanuk.api.model.Like;
import com.booleanuk.api.model.UserPostKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, UserPostKey> {
    @Query("SELECT COUNT(*) FROM Like l JOIN l.post p WHERE p.id = :postId")
    int findAmountOfLikesOnPost(int postId);

    Optional<Like> findByUserIdAndPostId(int userId, int postId);
}
