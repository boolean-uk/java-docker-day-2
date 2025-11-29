package com.booleanuk.api.repository;

import com.booleanuk.api.model.Repost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepostRepository extends JpaRepository<Repost, Integer> {
}
