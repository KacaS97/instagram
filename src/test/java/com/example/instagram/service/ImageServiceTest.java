package com.example.instagram.service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.example.instagram.entity.Image;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
public class ImageServiceTest {

  @InjectMocks
  private ImageService imageService;

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

}
