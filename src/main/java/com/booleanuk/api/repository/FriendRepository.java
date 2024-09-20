package com.booleanuk.api.repository;

import com.booleanuk.api.model.Friend;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FriendRepository extends JpaRepository<Friend, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Friend f WHERE f.u1.id = :userId OR f.u2.id = :userId")
    void deleteAllByUserId(@Param("userId") Integer userId);
}
