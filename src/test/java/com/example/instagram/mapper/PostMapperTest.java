package com.example.instagram.mapper;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.example.instagram.dto.PostBuildDto;
import com.example.instagram.dto.PostDto;
import com.example.instagram.entity.Image;
import com.example.instagram.entity.Post;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {PostMapperImpl.class, ImageMapperImpl.class})
class PostMapperTest {

  @Autowired
  private PostMapper postMapper;

  @Test
  void testToDto() {
    // given
    Post post = new Post();
    post.setId(1L);
    post.setDescription("description");
    post.setUserName("username");

    Image image = new Image();
    image.setName("image");
    image.setId(2L);
    image.setContent("content".getBytes(StandardCharsets.UTF_8));
    post.setImage(image);

    // when
    PostDto postDto = postMapper.toDto(post);

    // then
    assertEquals(postDto.id(), post.getId());
    assertEquals(postDto.description(), post.getDescription());
    assertEquals(postDto.imageDto().id(), image.getId());
    assertEquals(postDto.imageDto().name(), image.getName());
    assertArrayEquals(postDto.imageDto().content(), image.getContent());
    assertEquals(postDto.userName(), post.getUserName());
  }

  @Test
  void testToEntity() {
    // given
    PostBuildDto dto = new PostBuildDto("description");

    // when
    Post entity = postMapper.toEntity(dto);

    //then
    assertNull(entity.getId());
    assertEquals(dto.description(), entity.getDescription());
    assertNull(entity.getUserName());
  }


  @Test
  public void testUpdateEntityFromBuildDto() {
    // given
    Post post = new Post();
    post.setId(1L);
    post.setDescription("old desc");
    post.setUserName("old username");

    PostBuildDto postBuildDto = new PostBuildDto("new desc");

    /// when
    postMapper.updateEntityFromBuildDto(postBuildDto, post);

    // then
    assertEquals(postBuildDto.description(), post.getDescription());
    assertEquals("old username", post.getUserName());
  }

}