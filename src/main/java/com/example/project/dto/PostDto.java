package com.example.project.dto;

import lombok.Data;

@Data
public class PostDto {
    private String title;
    private String content;
    private String userId; // 작성자 ID
}
