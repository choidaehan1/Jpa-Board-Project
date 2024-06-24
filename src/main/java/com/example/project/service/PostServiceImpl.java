// PostServiceImpl.java
package com.example.project.service;

import com.example.project.dto.PostDto;
import com.example.project.entity.Post;
import com.example.project.entity.User;
import com.example.project.repository.PostRepository;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public void save(PostDto postDto) {
        User user = userRepository.findByUserId(postDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + postDto.getUserId()));

        Post post = Post.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .createdAt(LocalDateTime.now())
                .user(user)
                .build();
        postRepository.save(post);
    }


    @Override
    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + id));
    }

    @Override
    public void deletePostById(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public void updatePost(Post post) {
        Post existingPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID: " + post.getId()));

        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        existingPost.setCreatedAt(LocalDateTime.now());

        postRepository.save(existingPost);
    }
}
