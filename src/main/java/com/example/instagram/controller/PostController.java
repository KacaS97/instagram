package com.example.instagram.controller;

import com.example.instagram.dto.PostBuildDto;
import com.example.instagram.dto.PostDto;
import com.example.instagram.entity.Post;
import com.example.instagram.exception.NotFoundException;
import com.example.instagram.mapper.PostMapper;
import com.example.instagram.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;
  private final PostMapper postMapper;

  @GetMapping(value = "/{id}")
  public PostDto getById(@PathVariable long id) {
    return postService.getById(id)
        .map(postMapper::toDto)
        .orElseThrow(NotFoundException::new);
  }

  @PutMapping(value = "/{id}")
  public PostDto updatePost(@PathVariable long id, @RequestBody PostBuildDto postBuildDto) {
    Post post = postService.getById(id).orElseThrow(NotFoundException::new);
    postMapper.updateEntityFromBuildDto(postBuildDto, post);
    postService.updatePost(post);
    return postMapper.toDto(post);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public PostDto createPost(@RequestBody PostBuildDto postBuildDto) {
    Post post = postMapper.toEntity(postBuildDto);
    OAuth2User oauth2User = (OAuth2User) SecurityContextHolder.getContext().getAuthentication()
        .getPrincipal();
    String userName = oauth2User.getAttribute("login");
    post.setUserName(userName);
    Post createdPost = postService.createPost(post);
    return postMapper.toDto(createdPost);
  }

  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deletePost(@PathVariable long id) {
    postService.deletePost(id);
  }

  @GetMapping
  public Page<PostDto> getAllPosts(@PageableDefault Pageable pageable) {
    return postService.getAllPosts(pageable).map(postMapper::toDto);
  }
}
