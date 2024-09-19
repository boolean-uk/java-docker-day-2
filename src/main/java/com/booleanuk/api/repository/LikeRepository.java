package com.booleanuk.api.repository;

import com.booleanuk.api.model.Like;
import com.booleanuk.api.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    public List<Like> findAllByPostId(int postId);
}
