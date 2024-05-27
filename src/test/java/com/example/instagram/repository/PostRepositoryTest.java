package com.example.instagram.repository;

import com.example.instagram.entity.Post;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
class PostRepositoryTest {

  @Autowired
  private PostRepository postRepository;

  @Test
  void findAll_shouldReturnPagedPosts() {
    // given
    Post post1 = new Post();
    post1.setDescription("Post 1 Description");

    Post post2 = new Post();
    post2.setDescription("Post 2 Description");

    postRepository.save(post1);
    postRepository.save(post2);

    Pageable pageable = PageRequest.of(0, 2);

    // when
    Page<Post> result = postRepository.findAll(pageable);

    // then
    assertEquals(2, result.getTotalElements());
    assertEquals(1, result.getTotalPages());
    assertEquals(2, result.getContent().size());
  }
}
