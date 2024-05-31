package com.example.instagram.controller;

import com.example.instagram.entity.Image;
import com.example.instagram.exception.NotFoundException;
import com.example.instagram.service.ImageService;
import com.example.instagram.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/posts/{postId}/images")
public class ImageController {

  private final ImageService imageService;
  private final PostService postService;

  public ImageController(ImageService imageService, PostService postService) {
    this.imageService = imageService;
    this.postService = postService;
  }

  // curl -X POST http://localhost:8080/posts/1/images -F "multipartFile=@image.png"
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createImage(@PathVariable long postId, @RequestParam MultipartFile multipartFile) {
    postService.getById(postId)
        .ifPresentOrElse(post -> {
              Image image = imageService.buildImage(multipartFile);
              post.setImage(image);
              postService.updatePost(post);
            },
            () -> {
              throw new NotFoundException();
            });
  }

}
