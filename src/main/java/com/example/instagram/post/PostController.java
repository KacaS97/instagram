package com.example.instagram.post;

import com.example.instagram.exception.NotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
public class PostController {

  private final PostService postService;
  private final PostMapper postMapper;

  public PostController(PostService postService, PostMapper postMapper) {
    this.postService = postService;
    this.postMapper = postMapper;
  }

  @GetMapping(value = "/{id}")
  public PostDto getById(@PathVariable long id) {
    return postService.getById(id)
      .map(postMapper::toDto)
      .orElseThrow(NotFoundException::new);
  }

}
