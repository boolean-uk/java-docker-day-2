package com.booleanuk.api.model.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostSingleDTO {
    private int id;

    private String title;

    private String content;

    private UserSingleDTO author;
}
