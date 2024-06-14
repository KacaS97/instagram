package com.example.instagram.mapper;

import com.example.instagram.dto.CommentBuildDto;
import com.example.instagram.dto.CommentDto;
import com.example.instagram.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CommentMapper {

  @Mapping(source = "post.id", target = "postId")
  CommentDto toDto(Comment comment);

  @Mapping(source = "postId", target = "post")
  Comment toEntity(CommentDto commentDto);

  @Mapping(target = "post", ignore = true)
  void updateEntityFromBuildDto(CommentBuildDto dto, @MappingTarget Comment entity);
}
