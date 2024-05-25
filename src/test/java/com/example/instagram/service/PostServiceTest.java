package com.example.instagram.service;

import com.example.instagram.entity.Post;
import com.example.instagram.exception.NotFoundException;
import com.example.instagram.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    // given
    long postId = 1L;
    when(postRepository.existsById(postId)).thenReturn(true);

    // when
    postService.deletePost(postId);

    // then
    verify(postRepository).deleteById(postId);
  }

  @Test
  void testDeletePostNotFound() {
    // given
    long postId = 1L;
    when(postRepository.existsById(postId)).thenReturn(false);

    // when
    assertThrows(NotFoundException.class, () -> postService.deletePost(postId));

    // then
    verify(postRepository, never()).deleteById(postId);
  }

}