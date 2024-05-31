package com.example.instagram.service;

import com.example.instagram.entity.Image;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

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
}
