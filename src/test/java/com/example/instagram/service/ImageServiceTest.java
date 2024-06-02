package com.example.instagram.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.instagram.controller.ImageController;
import com.example.instagram.entity.Image;
import com.example.instagram.entity.Post;
import com.example.instagram.exception.NotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

  private final ImageService imageService = new ImageService();

  @Mock
  private PostService postService;

  @InjectMocks
  private ImageController imageController;

  @Test
  public void givenBuildImage_whenNoExceptionOccurs_thenImageIsReturned() {
    // given
    byte[] content = "content".getBytes(StandardCharsets.UTF_8);
    MockMultipartFile multipartFile = new MockMultipartFile("file", "image.jpg",
        MediaType.IMAGE_JPEG_VALUE, content);

    // when
    Image image = imageService.buildImage(multipartFile);

    // then
    assertEquals("image.jpg", image.getName());
    assertArrayEquals(content, image.getContent());
  }

  @Test
  public void givenBuildImage_whenExceptionOccurs_thenExceptionIsThrown() throws IOException {
    // given
    MockMultipartFile multipartFile = mock(MockMultipartFile.class);

    // when
    when(multipartFile.getBytes()).thenThrow(new IOException());

    // then
    assertThrows(RuntimeException.class, () -> imageService.buildImage(multipartFile));
  }

  @Test
  void givenValidPostId_whenDeleteImage_thenImageDeleted() {
    // Arrange
    long postId = 1L;
    Post post = new Post();
    post.setId(postId);
    post.setImage(new Image());
    when(postService.getById(postId)).thenReturn(Optional.of(post));

    // Act
    imageController.deleteImage(postId);

    // Assert
    verify(postService).updatePost(post);
    assert (post.getImage() == null);
  }

  @Test
  void givenInvalidPostId_whenDeleteImage_thenThrowNotFoundException() {
    // Arrange
    long postId = 999L;
    when(postService.getById(postId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NotFoundException.class, () -> imageController.deleteImage(postId));
  }
}

