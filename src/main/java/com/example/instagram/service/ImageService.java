package com.example.instagram.service;

import com.example.instagram.entity.Image;
import com.example.instagram.repository.ImageRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {

  private final ImageRepository imageRepository;

  public Image buildImage(MultipartFile multipartFile) {
    Image image = new Image();
    try {
      image.setContent(multipartFile.getBytes());
      image.setName(multipartFile.getOriginalFilename());
    } catch (IOException e) {
      throw new RuntimeException("Cannot read multipartFile", e);
    }
    return image;
  }

  public void deleteImage(Image image) {
    imageRepository.delete(image);
  }
}
