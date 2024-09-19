package com.booleanuk.api.dto;

import com.booleanuk.api.model.Post;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class UserDTO {
    private int id;

    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    private List<PostSummaryDTO> posts;
}
