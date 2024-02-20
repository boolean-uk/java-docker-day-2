package com.booleanuk.api.repository;

import com.booleanuk.api.model.ERole;
import com.booleanuk.api.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole name);
    boolean existsByName(ERole name);
}
