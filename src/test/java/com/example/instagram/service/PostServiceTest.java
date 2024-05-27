package com.example.instagram.service;

import com.example.instagram.entity.Post;
import com.example.instagram.exception.NotFoundException;
import com.example.instagram.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

  @InjectMocks
  private PostService postService;

  @Mock
  private PostRepository postRepository;

  @Test
  void whenGetById_thenReturnPost() {
    // given
    long id = 1L;
    Post post = new Post();
    post.setId(id);
    String description = "description";
    post.setDescription(description);

    // when
    when(postRepository.findById(id)).thenReturn(Optional.of(post));
    Optional<Post> result = postService.getById(id);

    // then
    assertTrue(result.isPresent());
    assertEquals(result.get().getId(), id);
    assertEquals(result.get().getDescription(), description);
  }

  @Test
  void whenGetById_thenReturnEmpty() {
    // given
    long id = 1L;

    // when
    when(postRepository.findById(id)).thenReturn(Optional.empty());
    Optional<Post> result = postService.getById(id);

    // then
    assertTrue(result.isEmpty());
  }

  @Test
  void whenPostExists_thenUpdateAndReturn() {
    // given
    Post post = new Post();
    post.setId(1L);
    post.setDescription("new description");

    Post existingPost = new Post();
    existingPost.setId(1L);
    existingPost.setDescription("old description");

    // when
    when(postRepository.findById(post.getId())).thenReturn(Optional.of(existingPost));
    when(postRepository.save(post)).thenReturn(post);
    Post updatedPost = postService.updatePost(post);

    // then
    verify(postRepository).save(post);
    verify(postRepository).findById(post.getId());
    assertEquals(post.getDescription(), updatedPost.getDescription());
  }

  @Test
  void whenPostDoesNotExist_thenThrowException() {
    // given
    Post post = new Post();
    post.setId(1L);
    post.setDescription("description");

    // when
    when(postRepository.findById(post.getId())).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> postService.updatePost(post));

    // then
    verify(postRepository, times(0)).save(post);
    verify(postRepository).findById(post.getId());
  }

  @Test
  void testDeletePostSuccess() {
    long postId = 1L;
    when(postRepository.existsById(postId)).thenReturn(true);
    postService.deletePost(postId);
    verify(postRepository).deleteById(postId);
  }

  @Test
  void testDeletePostNotFound() {
    long postId = 1L;
    when(postRepository.existsById(postId)).thenReturn(false);
    assertThrows(NotFoundException.class, () -> postService.deletePost(postId));
    verify(postRepository, never()).deleteById(postId);
  }

  @Test
  void whenGetAllPosts_thenReturnAllPosts() {
    // given
    Post post1 = new Post();
    post1.setId(1L);
    post1.setDescription("Post 1 Description");

    Post post2 = new Post();
    post2.setId(2L);
    post2.setDescription("Post 2 Description");

    List<Post> expectedPosts = asList(post1, post2);

    // when
    when(postRepository.findAll()).thenReturn(expectedPosts);
    List<Post> actualPosts = postService.getAllPosts();

    // then
    assertEquals(expectedPosts, actualPosts);
  }

  @Test
  void whenGetAllPosts_thenReturnPagedPosts() {
    // given
    Post post1 = new Post();
    post1.setId(1L);
    post1.setDescription("Post 1 Description");

    Post post2 = new Post();
    post2.setId(2L);
    post2.setDescription("Post 2 Description");

    List<Post> expectedPosts = asList(post1, post2);
    Pageable pageable = PageRequest.of(1, 2);
    Page<Post> expectedPage = new PageImpl<>(expectedPosts, pageable, expectedPosts.size());

    // when
    when(postRepository.findAll(pageable)).thenReturn(expectedPage);
    Page<Post> actualPage = postService.getAllPosts(pageable);

    // then
    assertEquals(expectedPage.getContent(), actualPage.getContent());
    assertEquals(expectedPage.getTotalElements(), actualPage.getTotalElements());
    assertEquals(expectedPage.getNumber(), actualPage.getNumber());
  }
}