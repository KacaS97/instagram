package com.example.instagram.mapper;

import com.example.instagram.dto.ImageDto;
import com.example.instagram.entity.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImageMapper {

  ImageDto toDto(Image image);

  @Mapping(target = "id", ignore = true)
  Image toEntity(ImageDto imageDto);

}
