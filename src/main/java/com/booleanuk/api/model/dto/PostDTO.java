package com.booleanuk.api.model.dto;

import com.booleanuk.api.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private int byUser;
    private String title;
    private String content;
}
