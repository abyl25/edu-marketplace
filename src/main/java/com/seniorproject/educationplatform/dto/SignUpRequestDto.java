package com.seniorproject.educationplatform.dto;

import lombok.Data;

@Data
public class SignUpRequestDto {
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;
    private int userType; // admin = 1,  instructor = 2, student = 3
}
