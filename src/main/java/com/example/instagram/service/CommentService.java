package com.example.instagram.service;

import com.example.instagram.entity.Comment;
import com.example.instagram.entity.Post;
import com.example.instagram.exception.NotFoundException;
import com.example.instagram.repository.CommentRepository;
import com.example.instagram.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;

  public List<Comment> getAllComments() {
    return commentRepository.findAll();
  }

  public Comment getCommentById(Long id) {
    return commentRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Comment not found"));
  }

  public Comment createComment(Long postId, Comment comment) {
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new NotFoundException("Post not found"));
    comment.setPost(post);
    return commentRepository.save(comment);
  }

  public Comment updateComment(Long id, Comment commentDetails) {
    Comment comment = getCommentById(id);
    comment.setContent(commentDetails.getContent());
    comment.setUserName(commentDetails.getUserName());
    return commentRepository.save(comment);
  }

  public void deleteComment(Long id) {
    Comment comment = getCommentById(id);
    commentRepository.delete(comment);
  }
}
