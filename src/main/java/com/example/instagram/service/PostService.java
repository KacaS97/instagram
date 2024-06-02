package com.example.instagram.service;

import com.example.instagram.entity.Post;
import com.example.instagram.exception.NotFoundException;
import com.example.instagram.repository.PostRepository;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final ImageService imageService;

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

  @Transactional
  public void deleteImage(Post post) {
    imageService.deleteImage(post.getImage());
    post.setImage(null);
    updatePost(post);
  }

}
