package com.booleanuk.api.library.repository;

import com.booleanuk.api.library.models.ERole;
import com.booleanuk.api.library.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
}
