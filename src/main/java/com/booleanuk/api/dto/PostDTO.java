package com.booleanuk.api.dto;

import com.booleanuk.api.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostDTO {
    private String message;
    private Integer user;
}
