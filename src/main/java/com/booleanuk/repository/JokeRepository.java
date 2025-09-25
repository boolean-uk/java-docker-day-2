package com.booleanuk.repository;

import com.booleanuk.model.Joke;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JokeRepository extends JpaRepository<Joke, Integer> {
}