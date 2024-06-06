package com.example.instagram.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.instagram.config.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ContextConfiguration(classes = {TestSecurityConfig.class})
class PostControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, statements = "INSERT INTO posts (id, description) VALUES (1, 'My first post')")
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, statements = "DELETE FROM posts")
  void givenPostRetrieval_whenPostExists_thenPostIsReturned() throws Exception {
    // when
    mockMvc.perform(get("/posts/1"))
        // then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.description", is("My first post")));
  }

  @Test
  void givenPostRetrieval_whenPostDoesNotExist_thenNotFoundIsReturned() throws Exception {
    // when
    mockMvc.perform(get("/posts/1"))
        // then
        .andExpect(status().isNotFound());
  }

  @Test
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, statements = "DELETE FROM posts")
  void whenCreatePost_thenPostIsCreated() throws Exception {
    // given
    String postJson = """
        {
            "description": "Test description"
        }
        """;

    // when
    mockMvc.perform(post("/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .content(postJson))
        // then
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())  // Check that id exists
        .andExpect(jsonPath("$.description").value("Test description"));
  }

  @Test
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, statements = "insert into posts(id, description) values(1, 'old description')")
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, statements = "DELETE FROM posts")
  void givenPostUpdate_whenPostExists_thenPostIsUpdatedAndReturned() throws Exception {
    // given
    String postDto = """
        {
          "id": 1,
          "description": "new description"
        }
        """;

    // when
    mockMvc.perform(put("/posts/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(postDto))
        // then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.description").value("new description"));
  }

  @Test
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, statements = "INSERT INTO posts (id, description) VALUES (1, 'Test post')")
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, statements = "DELETE FROM posts")
  void givenPostDeletion_whenPostExists_thenPostIsDeleted() throws Exception {
    // when
    mockMvc.perform(delete("/posts/1")
            .contentType(MediaType.APPLICATION_JSON))
        // then
        .andExpect(status().isNoContent());
  }

  @Test
  void givenDeletion_whenPostDoesNotExist_thenNotFoundIsReturned() throws Exception {
    // given
    long nonExistentPostId = 1L;

    // when
    mockMvc.perform(delete("/posts/{id}", nonExistentPostId)
            .contentType(MediaType.APPLICATION_JSON))
        // then
        .andExpect(status().isNotFound());
  }


  @Test
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, statements = "INSERT INTO posts (id, description) VALUES (1, 'My first post'), (2, 'My second post')")
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, statements = "DELETE FROM posts")
  void whenGetAllPosts_thenAllPostsAreReturned() throws Exception {
    // when
    mockMvc.perform(get("/posts"))
        // then
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(2)))
        .andExpect(jsonPath("$.content.[0].id", is(1)))
        .andExpect(jsonPath("$.content.[0].description", is("My first post")))
        .andExpect(jsonPath("$.content.[1].id", is(2)))
        .andExpect(jsonPath("$.content.[1].description", is("My second post")));
  }
}