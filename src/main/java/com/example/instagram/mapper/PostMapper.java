package com.example.instagram.mapper;

import com.example.instagram.dto.PostDto;
import com.example.instagram.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ImageMapper.class})
public interface PostMapper {

  // TODO modify test
  @Mapping(target = "imageDto", source = "image")
  PostDto toDto(Post post);

  // TODO modify test
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "image", source = "imageDto")
  Post toEntity(PostDto postDto);

  // TODO modify test
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "image", source = "imageDto")
  void updateEntityFromDto(PostDto postDto, @MappingTarget Post post);
}
