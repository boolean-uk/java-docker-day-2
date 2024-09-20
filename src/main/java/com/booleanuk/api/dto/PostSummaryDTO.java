package com.booleanuk.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PostSummaryDTO {
    private int id;

    @NotBlank
    @Length(min = 1, max = 300)
    private String content;
}
