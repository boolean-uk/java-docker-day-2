package com.booleanuk.api.repository;

import com.booleanuk.api.model.Post;
import com.booleanuk.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Integer> {
	List<Post> findByUser(User user);
}
