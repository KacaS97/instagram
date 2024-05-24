package com.example.instagram.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, statements = "DELETE FROM posts")
class PostControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, statements = "INSERT INTO posts (id, description) VALUES (1, 'My first post')")
  void whenPostExists_thenReturnsPost() throws Exception {
    mockMvc.perform(get("/posts/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.description", is("My first post")));
  }

  @Test
  void whenPostDoesNotExist_thenReturnsNotFound() throws Exception {
    mockMvc.perform(get("/posts/1"))
        .andExpect(status().isNotFound());
  }

  @Test
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
        .andExpect(jsonPath("$.description").value("Test description"));
  }

  @Test
  @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, statements = "insert into posts(id, description) values(1, 'old description')")
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
        .andExpect(jsonPath("$.description").value("new description"));
  }
}
