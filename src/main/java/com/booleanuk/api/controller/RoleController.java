package com.booleanuk.api.controller;

import com.booleanuk.api.model.ERole;
import com.booleanuk.api.model.Role;
import com.booleanuk.api.payload.response.Response;
import com.booleanuk.api.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.booleanuk.api.model.ERole.*;

@RestController
@RequestMapping("roles")
public class RoleController {
    @Autowired
    RoleRepository roleRepository;

    @GetMapping
    public ResponseEntity<?> createRoles()    {
        if(this.roleRepository.findAll().size() < 3)    {
            Role userRole = new Role(ROLE_USER);
            Role modRole = new Role(ROLE_MODERATOR);
            Role adminRol = new Role(ROLE_ADMIN);

            this.roleRepository.save(userRole);
            this.roleRepository.save(modRole);
            this.roleRepository.save(adminRol);

            return new ResponseEntity<>("Roles have been added successfully", HttpStatus.CREATED);
        }

        return ResponseEntity.ok("Roles already in database");
    }
}
