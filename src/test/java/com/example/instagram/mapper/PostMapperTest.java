package com.example.instagram.mapper;

import com.example.instagram.dto.PostDto;
import com.example.instagram.entity.Post;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PostMapperTest {

  PostMapper postMapper = Mappers.getMapper(PostMapper.class);

  @Test
  void toDto() {
    // given
    Post post = new Post();
    post.setId(1L);
    String description = "description";
    post.setDescription(description);

    // when
    PostDto postDto = postMapper.toDto(post);

    // then
    assertEquals(postDto.getId(), post.getId());
    assertEquals(postDto.getDescription(), post.getDescription());
  }

  @Test
  public void toEntity() {
    // given
    PostDto postDto = new PostDto();

    // when
    Post post = postMapper.toEntity(postDto);

    // then
    assertEquals(postDto.getId(), post.getId());
    assertEquals(post.getDescription(), postDto.getDescription());
  }
}