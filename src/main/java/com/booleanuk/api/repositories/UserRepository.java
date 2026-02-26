package com.booleanuk.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.booleanuk.api.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}
