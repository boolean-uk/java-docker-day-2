package com.booleanuk.api.repository;

import com.booleanuk.api.model.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {
    List<Friendship> findByUserId(int userId);

    List<Friendship> findAllByUserId(int userId);
}