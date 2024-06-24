package com.example.project.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    private String userId;
    private String userName;
    private String userPw;
    private LocalDate userBirthdate;
    private String userEmail;
}
