package com.booleanuk.api.repositories;

import com.booleanuk.api.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Integer>  {
}
