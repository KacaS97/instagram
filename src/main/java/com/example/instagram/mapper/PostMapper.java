package com.example.instagram.mapper;

import com.example.instagram.entity.Post;
import com.example.instagram.dto.PostDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

  PostDto toDto(Post post);

  @Mapping(target = "id", ignore = true)
  Post toEntity(PostDto postDto);
}
