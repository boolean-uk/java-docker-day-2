package com.booleanuk.api.repositories;

import com.booleanuk.api.models.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InteractionRepository extends JpaRepository<Interaction, Integer> {
}
