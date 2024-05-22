package com.example.instagram.post;

import com.example.instagram.entity.Post;
import com.example.instagram.mapper.PostMapper;
import com.example.instagram.dto.PostDto;
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

}