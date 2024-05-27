package com.example.instagram.service;

import com.example.instagram.entity.Post;
import com.example.instagram.exception.NotFoundException;
import com.example.instagram.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

import java.util.List;
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

  public Post updatePost(Post post) {
    getById(post.getId()).orElseThrow(NotFoundException::new);
    return postRepository.save(post);
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

  public List<Post> getAllPosts() {
    return postRepository.findAll();
  }

  public Page<Post> getAllPosts(Pageable pageable) {
    return postRepository.findAll(pageable);
  }
}
