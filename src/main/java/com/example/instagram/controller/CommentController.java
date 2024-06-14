package com.example.instagram.controller;

import com.example.instagram.dto.CommentBuildDto;
import com.example.instagram.dto.CommentDto;
import com.example.instagram.entity.Comment;
import com.example.instagram.entity.Post;
import com.example.instagram.exception.NotFoundException;
import com.example.instagram.mapper.CommentMapper;
import com.example.instagram.service.CommentService;
import com.example.instagram.service.PostService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;
  private final CommentMapper commentMapper;
  private final PostService postService;

  @GetMapping
  public List<CommentDto> getAllComments() {
    return commentMapper.toDto(commentService.getAllComments());
  }

  @GetMapping("/{id}")
  public CommentDto getCommentById(@PathVariable Long id) {
    return commentService.getCommentById(id)
        .map(commentMapper::toDto)
        .orElseThrow(NotFoundException::new);
  }

  @PostMapping("/post/{postId}")
  @ResponseStatus(HttpStatus.CREATED)
  public CommentDto createComment(@PathVariable Long postId,
      @RequestBody CommentBuildDto commentBuildDto) {
    Post post = postService.getById(postId).orElseThrow(NotFoundException::new);
    Comment comment = commentMapper.toEntity(commentBuildDto);
    comment.setPost(post);
    return commentMapper.toDto(commentService.createComment(comment));
  }

  @PutMapping("/{id}")
  public CommentDto updateComment(@PathVariable Long id,
      @RequestBody CommentBuildDto commentBuildDto) {
    Comment comment = commentService.getCommentById(id).orElseThrow(NotFoundException::new);
    commentMapper.updateEntityFromBuildDto(commentBuildDto, comment);
    return commentMapper.toDto(commentService.updateComment(comment));
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteComment(@PathVariable Long id) {
    commentService.deleteComment(id);
  }
}
