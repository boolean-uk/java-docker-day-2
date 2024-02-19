package com.booleanuk.api.repository;

import com.booleanuk.api.model.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Integer> {
}
