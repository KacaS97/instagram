package com.example.instagram.mapperTest;

import com.example.instagram.entity.Post;
import com.example.instagram.mapper.PostMapper;
import com.example.instagram.dto.PostDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

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
  public void testToDto() {
    // Arrange
    Post post = new Post();
    post.setId(1L);
    post.setDescription("Test Description");

    // Act
    PostDto postDto = postMapper.toDto(post);

    // Assert
    assertNotNull(postDto);
    assertEquals(post.getId(), postDto.getId());
    assertEquals(post.getDescription(), postDto.getDescription());
  }

  @Test
  public void testToEntity() {
    // Arrange
    PostDto postDto = new PostDto();
    postDto.setId(1L);
    postDto.setDescription("Test Description");

    // Act
    Post post = postMapper.toEntity(postDto);

    // Assert
    assertNotNull(post);
    assertEquals(0, post.getId());  // ID should be ignored and default to 0
    assertEquals(postDto.getDescription(), post.getDescription());
  }
}