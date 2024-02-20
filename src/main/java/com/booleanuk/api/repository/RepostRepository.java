package com.booleanuk.api.repository;

import com.booleanuk.api.model.Repost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepostRepository extends JpaRepository<Repost, Integer> {
    List<Repost> findByPostId(int postId);

    List<Repost> findAllByPostId(int postId);
}