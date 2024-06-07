package com.example.instagram.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @WithMockUser
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
  @WithMockUser
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

    // Mock user
    OAuth2User oauth2User = mock(OAuth2User.class);
    when(oauth2User.getAttribute("login")).thenReturn("username");
    OAuth2AuthenticationToken auth = new OAuth2AuthenticationToken(oauth2User,
        Collections.emptyList(), "client");
    SecurityContextHolder.getContext().setAuthentication(auth);

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
  @WithMockUser
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
  @WithMockUser
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
  @WithMockUser
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
  @WithMockUser
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