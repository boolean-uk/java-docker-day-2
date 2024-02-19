package com.booleanuk.api.library.controllers;

import com.booleanuk.api.library.models.ERole;
import com.booleanuk.api.library.models.Post;
import com.booleanuk.api.library.models.Role;
import com.booleanuk.api.library.models.User;
import com.booleanuk.api.library.payload.response.MessageResponse;
import com.booleanuk.api.library.payload.response.PostResponse;
import com.booleanuk.api.library.payload.response.Response;
import com.booleanuk.api.library.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("roles")
public class RoleController {

    @Autowired
    RoleRepository roleRepository;
    @PostMapping("add")
    public MessageResponse addRoles() {
        this.roleRepository.save(new Role(ERole.ROLE_ADMIN));
        this.roleRepository.save(new Role(ERole.ROLE_USER));
        this.roleRepository.save(new Role(ERole.ROLE_MODERATOR));
        return new MessageResponse("roles created");
    }
}
