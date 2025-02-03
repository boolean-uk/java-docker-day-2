package com.booleanuk.api;

import com.booleanuk.api.model.ERole;
import com.booleanuk.api.model.Role;
import com.booleanuk.api.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main implements CommandLineRunner {
    @Autowired
    private RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        if (!this.roleRepository.existsByName(ERole.ROLE_USER)) {
            this.roleRepository.save(new Role(ERole.ROLE_USER));
        }
        if (!this.roleRepository.existsByName(ERole.ROLE_ADMIN)) {
            this.roleRepository.save(new Role(ERole.ROLE_ADMIN));
        }
        if (!this.roleRepository.existsByName(ERole.ROLE_MODERATOR)) {
            this.roleRepository.save(new Role(ERole.ROLE_MODERATOR));
        }
    }
}
