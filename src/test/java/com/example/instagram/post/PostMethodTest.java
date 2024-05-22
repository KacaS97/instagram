package com.example.instagram.post;

import com.example.instagram.controller.PostController;
import com.example.instagram.dto.PostDto;
import com.example.instagram.entity.Post;
import com.example.instagram.mapper.PostMapper;
import com.example.instagram.service.PostService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
public class PostMethodTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PostService postService;

  @MockBean
  private PostMapper postMapper;

  @Test
  public void testCreatePost() throws Exception {
    PostDto postDto = new PostDto();
    postDto.setId(1L);
    postDto.setDescription("Test description");

    Post post = new Post();
    post.setId(1L);
    post.setDescription("Test description");

    Mockito.when(postMapper.toEntity(any())).thenReturn(post);
    Mockito.when(postService.createdPost(any())).thenReturn(post);
    Mockito.when(postMapper.toDto(any())).thenReturn(postDto);

    mockMvc.perform(MockMvcRequestBuilders.post("/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"id\": 1, \"description\": \"Test description\"}"))
        .andExpect(status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
        .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("Test description"));
  }
}