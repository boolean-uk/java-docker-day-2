package com.booleanuk.api.repositories;

import com.booleanuk.api.models.Friend;
import com.booleanuk.api.models.FriendId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<Friend, FriendId> {
}
