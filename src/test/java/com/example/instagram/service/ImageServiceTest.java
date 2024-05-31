package com.example.instagram.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.instagram.entity.Image;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

public class ImageServiceTest {

  private final ImageService imageService = new ImageService();

  @Test
  public void whenBuildImage_thenThrowIOException() throws Exception {
    byte[] content = {1, 2, 3};
    MockMultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg",
        content);

    Image image = imageService.buildImage(multipartFile);

    assertNotNull(image);
    assertEquals("test.jpg", image.getName());
    assertArrayEquals(content, image.getContent());
  }

  @Test
  public void testBuildImage_IOException() {
    MockMultipartFile multipartFile = new MockMultipartFile(
        "file", "test.jpg", "image/jpeg", new byte[0]);

    assertDoesNotThrow(() -> imageService.buildImage(multipartFile));
  }
}

