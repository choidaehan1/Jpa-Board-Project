// PostService.java
package com.example.project.service;

import com.example.project.dto.PostDto;
import com.example.project.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    void save(PostDto postDto);
    Page<Post> getAllPosts(Pageable pageable);
    Post getPostById(Long id);
    void updatePost(Post post);
    void deletePostById(Long id);
}
