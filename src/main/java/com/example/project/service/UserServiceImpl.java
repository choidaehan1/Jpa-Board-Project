package com.example.project.service;

import com.example.project.dto.UserDto;
import com.example.project.entity.User;
import com.example.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User saveEntity(UserDto userDto) {
        User user = User.builder()
                .userId(userDto.getUserId())
                .userName(userDto.getUserName())
                .userPw(userDto.getUserPw())
                .userEmail(userDto.getUserEmail())
                .userBirthdate(userDto.getUserBirthdate())
                .build();
        return userRepository.save(user);
    }

    @Override
    public boolean login(String userId, String userPw) {
        return userRepository.findByUserId(userId)
                .map(user -> user.getUserPw().equals(userPw))
                .orElse(false);
    }

    @Override
    public boolean userExists(String userId) {
        return userRepository.findByUserId(userId).isPresent();
    }
}
