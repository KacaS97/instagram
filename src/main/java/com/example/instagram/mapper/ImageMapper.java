package com.example.instagram.mapper;

import com.example.instagram.dto.ImageDto;
import com.example.instagram.entity.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImageMapper {

  // TODO add test
  ImageDto toDto(Image image);

  // TODO add test
  @Mapping(target = "id", ignore = true)
  Image toEntity(ImageDto imageDto);

}
