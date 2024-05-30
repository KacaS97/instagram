package com.example.instagram.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.example.instagram.dto.PostDto;
import com.example.instagram.entity.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {PostMapperImpl.class, ImageMapperImpl.class})
class PostMapperTest {

  @Autowired
  private PostMapper postMapper;

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
  void toEntity() {
    // given
    PostDto postDto = new PostDto();
    postDto.setId(1);
    postDto.setDescription("description");

    // when
    Post entity = postMapper.toEntity(postDto);

    //then
    assertNull(entity.getId());
    assertEquals(postDto.getDescription(), entity.getDescription());
  }

}