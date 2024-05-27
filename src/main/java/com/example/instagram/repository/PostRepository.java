package com.example.instagram.repository;

import com.example.instagram.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>,
    PagingAndSortingRepository<Post, Long> {
}
