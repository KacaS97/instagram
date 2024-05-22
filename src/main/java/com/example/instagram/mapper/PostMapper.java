package com.example.instagram.mapper;

import com.example.instagram.entity.Post;
import com.example.instagram.dto.PostDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostMapper {

  PostDto toDto(Post post);

  Post toEntity(PostDto postDto);
}
