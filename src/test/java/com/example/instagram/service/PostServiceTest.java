package com.example.instagram.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.instagram.entity.Image;
import com.example.instagram.entity.Post;
import com.example.instagram.exception.NotFoundException;
import com.example.instagram.repository.PostRepository;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

  @InjectMocks
  private PostService postService;

  @Mock
  private PostRepository postRepository;

  @Mock
  private ImageService imageService;

  @Test
  void givenPostRetrieval_whenPostExists_thenPostOptionalIsReturned() {
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
  void givenPostRetrieval_whenPostDoesNotExist_thenEmptyOptionalIsReturned() {
    // given
    long id = 1L;

    // when
    when(postRepository.findById(id)).thenReturn(Optional.empty());
    Optional<Post> result = postService.getById(id);

    // then
    assertTrue(result.isEmpty());
  }

  @Test
  void givenPostDeletion_whenPostExists_thenDeletePost() {
    // given
    long postId = 1L;

    // when
    when(postRepository.existsById(postId)).thenReturn(true);
    postService.deletePost(postId);

    // then
    verify(postRepository).deleteById(postId);
  }

  @Test
  void givenPostDeletion_whenPostDoesNotExist_thenDoNothing() {
    // given
    long postId = 1L;

    // when
    when(postRepository.existsById(postId)).thenReturn(false);

    // then
    assertThrows(NotFoundException.class, () -> postService.deletePost(postId));
    verify(postRepository, never()).deleteById(postId);
  }

  @Test
  void whenGetAllPosts_thenAllPostsAreReturned() {
    // given
    Post post1 = new Post();
    post1.setId(1L);
    post1.setDescription("Post 1 Description");

    Post post2 = new Post();
    post2.setId(2L);
    post2.setDescription("Post 2 Description");

    Pageable pageable = mock(Pageable.class);
    PageImpl<Post> expectedPostsPage = new PageImpl<>(Arrays.asList(post1, post2));

    // when
    when(postRepository.findAll(pageable)).thenReturn(expectedPostsPage);
    Page<Post> actualPosts = postService.getAllPosts(pageable);

    // then
    assertEquals(expectedPostsPage, actualPosts);
  }

  @Test
  void whenDeletingImage_thenDeleteImageAndUpdatePost() {
    //given
    Post post = new Post();
    Image image = new Image();
    post.setImage(image);

    //when
    postService.deleteImage(post);

    //then
    assertNull(post.getImage());
    verify(imageService).deleteImage(image);
    verify(postRepository).save(post);
  }

  @Test
  void givenImageUpdate_whenImageDoesNotExist_thenPostIsUpdated() {
    // given
    Post post = new Post();
    Image newImage = new Image();

    // when
    postService.updateImage(post, newImage);

    // then
    assertEquals(newImage, post.getImage());
    verify(postRepository).save(post);
  }

  @Test
  void givenImageUpdate_whenImageExists_thenPostIsUpdated() {
    // given
    Post post = new Post();
    Image oldImage = new Image();
    post.setImage(oldImage);
    Image newImage = new Image();

    // when
    postService.updateImage(post, newImage);

    // then
    assertEquals(newImage, post.getImage());
    verify(postRepository).save(post);
    verify(imageService).deleteImage(oldImage);
  }

}
