package com.example.instagram.controller;

import com.example.instagram.dto.PostDto;
import com.example.instagram.entity.Post;
import com.example.instagram.mapper.PostMapper;
import com.example.instagram.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private PostService postService;

  @MockBean
  private PostMapper postMapper;

  @Test
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
      statements = "INSERT INTO posts (id, description) " +
          "VALUES (1, 'My first post')")
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
      statements = "DELETE FROM posts")
  void whenPostExists_thenReturnsPost() throws Exception {
    mockMvc.perform(get("/posts/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.description",
            is("My first post")));
  }

  @Test
  void whenPostDoesNotExist_thenReturnsNotFound() throws Exception {
    mockMvc.perform(get("/posts/1"))
        .andExpect(status().isNotFound());
  }

  @Test
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
      statements = "DELETE FROM posts")

  public void testCreatePost() throws Exception {
    String postJson = """
            {
                "description": "Test description"
            }
            """;

    mockMvc.perform(post("/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(postJson))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())  // Check that id exists
        .andExpect(jsonPath("$.description").
            value("Test description"));
  }

  @Test
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
      statements = "insert into posts(id, description) " +
          "values(1, 'old description')")
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
      statements = "DELETE FROM posts")
  void whenUpdatePost_thenUpdateAndReturnDto() throws Exception {
    String postDto = """
        {
          "id": 1,
          "description": "new description"
        }
        """;

    mockMvc.perform(put("/posts/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(postDto))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.description").
            value("new description"));
  }

  @Test
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
      statements = "INSERT INTO posts (id, description) " +
          "VALUES (1, 'Test post')")
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
      statements = "DELETE FROM posts")
  void whenDeletePostExists_thenReturnsNoContent() throws Exception {
    mockMvc.perform(delete("/posts/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  @Test
  void whenDeletePostDoesNotExist_thenReturnsNotFound() throws Exception {
    long nonExistentPostId = 1L;
    mockMvc.perform(delete("/posts/{id}", nonExistentPostId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }


  @Test
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
      statements = "INSERT INTO posts (id, description) " +
          "VALUES (1, 'My first post'), (2, 'My second post')")
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
      statements = "DELETE FROM posts")
  void whenGetAllPosts_thenReturnsAllPosts() throws Exception {
    mockMvc.perform(get("/posts"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].description",
            is("My first post")))
        .andExpect(jsonPath("$[1].id", is(2)))
        .andExpect(jsonPath("$[1].description",
            is("My second post")));
  }

  @Test
  void whenGetAllPosts_thenReturnPagedPosts() throws Exception {
    // given
    Post post1 = new Post();
    post1.setId(1L);
    post1.setDescription("Post 1 Description");

    Post post2 = new Post();
    post2.setId(2L);
    post2.setDescription("Post 2 Description");

    List<Post> posts = asList(post1, post2);
    Pageable pageable = PageRequest.of(1, 2);
    Page<Post> postPage = new PageImpl<>(posts, pageable, posts.size());

    PostDto postDto1 = new PostDto();
    postDto1.setId(1L);
    postDto1.setDescription("Post 1 Description");

    PostDto postDto2 = new PostDto();
    postDto2.setId(2L);
    postDto2.setDescription("Post 2 Description");

    when(postService.getAllPosts(pageable)).thenReturn(postPage);
    when(postMapper.toDto(post1)).thenReturn(postDto1);
    when(postMapper.toDto(post2)).thenReturn(postDto2);

    // when + then
    mockMvc.perform(get("/posts")
            .param("page", "1")
            .param("size", "2")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].id").value(1L))
        .andExpect(jsonPath("$.content[0].description").
            value("Post 1 Description"))
        .andExpect(jsonPath("$.content[1].id").
            value(2L))
        .andExpect(jsonPath("$.content[1].description").
            value("Post 2 Description"));
  }
}
