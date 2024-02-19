package com.booleanuk.api.repository;

import com.booleanuk.api.model.Like;
import com.booleanuk.api.model.Repost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RepostRepository extends JpaRepository<Repost, Integer> {
    @Query("SELECT COUNT(*) FROM Repost r JOIN r.post p WHERE p.id = :postId")
    int findAmountOfRepostsForPost(int postId);

    Optional<Repost> findByUserIdAndPostId(int userId, int postId);
}
