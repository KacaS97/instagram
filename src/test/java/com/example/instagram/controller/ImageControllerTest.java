package com.example.instagram.controller;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.instagram.entity.Image;
import com.example.instagram.entity.Post;
import com.example.instagram.repository.ImageRepository;
import com.example.instagram.repository.PostRepository;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ImageControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private PostRepository postRepository;
  @Autowired
  private ImageRepository imageRepository;

  @Test
  @WithMockUser
  @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = "insert into posts(id, description) values (10, 'desc')")
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, statements = {"delete from posts",
      "delete from images"})
  void givenImageCreation_whenPostExists_thenUpdatePostAndCreateImage() throws Exception {
    // given
    MockMultipartFile multipartFile = new MockMultipartFile("multipartFile", "image.jpg",
        MediaType.IMAGE_JPEG_VALUE, "content".getBytes());

    // when
    mockMvc.perform(multipart(HttpMethod.POST, "/posts/10/images", 1)
            .file(multipartFile)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isCreated());

    // then
    Post post = postRepository.findById(10L).orElseThrow();
    assertArrayEquals("content".getBytes(StandardCharsets.UTF_8), post.getImage().getContent());
    assertEquals("image.jpg", post.getImage().getName());
    assertEquals(1, imageRepository.findAll().size());
  }

  @Test
  @WithMockUser
  void givenImageDeletion_whenPostDoesNotExist_thenReturnNotFound() throws Exception {
    // when & then
    mockMvc.perform(delete("/posts/999/images")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  @WithMockUser
  @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = {
      "insert into images(id, name, content) values (10, 'image.jpg', 'content')",
      "insert into posts(id, description, image_id) values (10, 'desc', 10)"
  })
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, statements = {
      "delete from posts",
      "delete from images"
  })
  void givenImageDeletion_whenPostExists_thenDeleteImage() throws Exception {
    // When
    mockMvc.perform(delete("/posts/10/images")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    // Then
    assertEquals(0, imageRepository.findAll().size());
    assertNull(postRepository.findById(10L).orElseThrow().getImage());
  }

  @Test
  @WithMockUser
  @Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, statements = {
      "insert into images(id, name, content) values (10, 'old_image.jpg', 'old_content')",
      "insert into posts(id, description, image_id) values (10, 'desc', 10)"
  })
  @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, statements = {
      "delete from posts",
      "delete from images"
  })
  void givenImageUpdate_whenPostExists_thenUpdatePostAndDeleteOldImage() throws Exception {
    // given
    MockMultipartFile multipartFile = new MockMultipartFile("multipartFile", "new_image.jpg",
        MediaType.IMAGE_JPEG_VALUE, "new_content".getBytes());

    // when
    mockMvc.perform(multipart(HttpMethod.PUT, "/posts/10/images")
            .file(multipartFile)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isOk());

    // then
    Image updatedImage = imageRepository.findById(1L).orElseThrow();
    assertEquals("new_image.jpg", updatedImage.getName());
    assertArrayEquals("new_content".getBytes(StandardCharsets.UTF_8), updatedImage.getContent());
    assertEquals(1, imageRepository.findAll().size());
  }

  @Test
  @WithMockUser
  void givenImageUpdate_whenPostDoesNotExist_thenReturnNotFound() throws Exception {
    // given
    MockMultipartFile multipartFile = new MockMultipartFile("multipartFile", "new_image.jpg",
        MediaType.IMAGE_JPEG_VALUE, "new_content".getBytes());
    // When & Then
    mockMvc.perform(multipart(HttpMethod.PUT, "/posts/1/images")
            .file(multipartFile)
            .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isNotFound());
  }

}
