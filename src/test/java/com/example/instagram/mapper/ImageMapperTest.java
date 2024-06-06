package com.example.instagram.mapper;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.example.instagram.dto.ImageDto;
import com.example.instagram.entity.Image;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;


public class ImageMapperTest {

  private ImageMapper imageMapper = Mappers.getMapper(ImageMapper.class);

  @Test
  public void testToDto() {
    //given
    Image image = new Image();
    image.setId(1L);
    image.setName("test.jpg");
    image.setContent(new byte[]{1, 2, 3});

    //when
    ImageDto imageDto = imageMapper.toDto(image);

    //then
    assertNotNull(imageDto);
    assertEquals(1L, imageDto.id());
    assertEquals("test.jpg", imageDto.name());
    assertArrayEquals(new byte[]{1, 2, 3}, imageDto.content());
  }

  @Test
  public void testToEntity() {
    //given
    ImageDto imageDto = new ImageDto(1L, new byte[]{1, 2, 3}, "test.jpg");

    //when
    Image image = imageMapper.toEntity(imageDto);

    //then
    assertNotNull(image);
    assertNull(image.getId());
    assertEquals("test.jpg", image.getName());
    assertArrayEquals(new byte[]{1, 2, 3}, image.getContent());
  }
}


