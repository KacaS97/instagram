package com.example.instagram.dto;

public class PostDto {

  private long id;
  private String description;
  private ImageDto imageDto;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ImageDto getImageDto() {
    return imageDto;
  }

  public void setImageDto(ImageDto imageDto) {
    this.imageDto = imageDto;
  }
}
