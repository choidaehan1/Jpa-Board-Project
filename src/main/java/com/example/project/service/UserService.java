package com.example.project.service;

import com.example.project.dto.UserDto;
import com.example.project.entity.User;

public interface UserService {
    User saveEntity(UserDto userDto);
    boolean login(String userId, String userPw);
    boolean userExists(String userId); // 추가된 메서드
}
