package com.booleanuk.api.dto;

import com.booleanuk.api.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Post toEntity(PostDTO postDTO);

    PostDTO toDTO(Post post);
}
