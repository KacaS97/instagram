package com.example.instagram.post;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {

  PostDto toDto(Post post);
}
