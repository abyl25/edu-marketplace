package com.seniorproject.educationplatform.dto;

import lombok.Data;

@Data
public class LoginRequestDto {
    private String userName;
    private String password;
    private int userType; // student = 1, instructor = 2
}
