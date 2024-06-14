package com.example.instagram.dto;

import com.example.instagram.entity.Post;
import lombok.Data;

@Data
public class CommentDto {

  private Long id;
  private String content;
  private Long userId;
  private Post post;
}
