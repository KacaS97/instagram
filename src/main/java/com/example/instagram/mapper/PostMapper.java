package com.example.instagram.mapper;

import com.example.instagram.dto.PostBuildDto;
import com.example.instagram.dto.PostDto;
import com.example.instagram.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ImageMapper.class})
public interface PostMapper {

  @Mapping(target = "imageDto", source = "image")
  PostDto toDto(Post post);

  Post toEntity(PostBuildDto postBuildDto);

  void updateEntityFromBuildDto(PostBuildDto postBuildDto, @MappingTarget Post post);
}
