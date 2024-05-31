package com.example.instagram.service;

import com.example.instagram.entity.Post;
import com.example.instagram.exception.NotFoundException;
import com.example.instagram.repository.PostRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PostService {

  private final PostRepository postRepository;

  public PostService(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  public Optional<Post> getById(long id) {
    return postRepository.findById(id);
  }

  public void updatePost(Post post) {
    postRepository.save(post);
  }

  public Post createPost(Post post) {
    return postRepository.save(post);
  }

  public void deletePost(long id) {
    if (!postRepository.existsById(id)) {
      throw new NotFoundException();
    }
    postRepository.deleteById(id);
  }

  public Page<Post> getAllPosts(Pageable pageable) {
    return postRepository.findAll(pageable);
  }

}
