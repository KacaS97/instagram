package com.example.instagram.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.instagram.entity.Image;
import com.example.instagram.entity.Post;
import com.example.instagram.service.ImageService;
import com.example.instagram.service.PostService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@AutoConfigureMockMvc
public class ImageControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ImageService imageService;

  @MockBean
  private PostService postService;

  private Post post;

  @BeforeEach
  public void setUp() {
    post = new Post();
    post.setId(1L);
  }

  @Test
  void createImage_ShouldReturnCreatedStatus() throws Exception {
    MockMultipartFile multipartFile = new MockMultipartFile(
        "multipartFile", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "Test Image Content".getBytes());

    when(postService.getById(post.getId())).thenReturn(Optional.of(post));
    when(imageService.buildImage(any(MultipartFile.class))).thenReturn(new Image());

    mockMvc.perform(multipart("/posts/{postId}/images", post.getId())
            .file(multipartFile))
        .andExpect(status().isCreated());

    verify(postService, times(1)).getById(post.getId());
    verify(imageService, times(1)).buildImage(any(MultipartFile.class));
    verify(postService, times(1)).updatePost(post);
  }

  @Test
  void createImage_ShouldThrowNotFoundException() throws Exception {
    MockMultipartFile multipartFile = new MockMultipartFile(
        "multipartFile", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "Test Image Content".getBytes());

    when(postService.getById(post.getId())).thenReturn(Optional.empty());

    mockMvc.perform(multipart("/posts/{postId}/images", post.getId())
            .file(multipartFile))
        .andExpect(status().isNotFound());

    verify(postService, times(1)).getById(post.getId());
    verify(imageService, times(0)).buildImage(any(MultipartFile.class));
    verify(postService, times(0)).updatePost(any(Post.class));
  }
}
