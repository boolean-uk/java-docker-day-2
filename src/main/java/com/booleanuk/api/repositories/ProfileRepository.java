package com.booleanuk.api.repositories;

import com.booleanuk.api.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
}
