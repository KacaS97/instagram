package com.example.instagram.post;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostService {

  private final PostRepository postRepository;

  public PostService(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  public Optional<Post> getById(long id) {
    return postRepository.findById(id);
  }

}
